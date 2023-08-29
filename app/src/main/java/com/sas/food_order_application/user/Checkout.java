package com.sas.food_order_application.user;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sas.food_order_application.Adapter.CheckoutUserAdapter;
import com.sas.food_order_application.Adapter.HomeItemUserAdapter;
import com.sas.food_order_application.Model.CheckoutModel;
import com.sas.food_order_application.Model.HomeItemUserModel;
import com.sas.food_order_application.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Checkout extends AppCompatActivity {

    private static int totalAmount;
    FirebaseFirestore db;
    String restaurant,restaurantEmail;

    RecyclerView recyclerView;
    String useremail;
    TextView selectedRestaurant;
    public static TextView txtTotalAmount;
    List<CheckoutModel> checkoutList;
    String mail=UserLogin.emailid;
    CheckoutUserAdapter checkoutUserAdapter;
    Button btnPlaceOrder;

    private Handler handler = new Handler();
    private Runnable refreshRunnable;

    int tableNo=0;

    String orderId = UUID.randomUUID().toString();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        selectedRestaurant=findViewById(R.id.textSelectedRestaurant);
        recyclerView=findViewById(R.id.listOfItemAdded);
        btnPlaceOrder=findViewById(R.id.btntoOrder);
        txtTotalAmount=findViewById(R.id.textTotalAmount);

        Intent intent=getIntent();
        useremail=UserLogin.emailid;
        db=FirebaseFirestore.getInstance();
        Spinner spinner=findViewById(R.id.spinner);
        restaurant=intent.getStringExtra("Restaurant Name");
        selectedRestaurant.setText(restaurant);
        Log.d("resta",""+restaurant);


        db.collection("Restaurant").document(restaurant).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.get("Email"));
                    restaurantEmail=(String)documentSnapshot.get("Email");
                    db.collection("Admin").document(restaurantEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    long tablecount=(long) document.get("Tablecount");
                                    populateSpinner(spinner, tablecount);
                                }

                            }
                        }
                    });
                }
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        tableNo= (position+1);
//                        parent.setEnabled(false);
                        Log.d("doc","is "+tableNo);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(getApplicationContext(),"table selection is required",Toast.LENGTH_SHORT).show();
                    }
                });

                //recyclerview
                checkoutList=new ArrayList<>();
                setRecyclerViewList();
                checkoutUserAdapter=new CheckoutUserAdapter(Checkout.this,checkoutList);
                recyclerView.setAdapter(checkoutUserAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                setTotalAmount();
                btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id =generateOrderNumber();

                        myordersdatabase(id);
                        sendDataToAdmin(id);

                        Intent intent=new Intent(Checkout.this,orderplaced_splash.class);
                        startActivity(intent);
                        finish();


                    }


                });


            }
        });


    }

    private int generateOrderNumber() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());
        Date currentTime = new Date();
        String formattedTime = dateFormat.format(currentTime);

        Random random = new Random();
        int randomSuffix = random.nextInt(9000) + 1000; // Generates a random 4-digit number

        return randomSuffix;

    }

    private void myordersdatabase(int id) {

        HashMap<String,Integer> listOrder = new HashMap<>();
        for (CheckoutModel c:CheckoutUserAdapter.checkoutModelList){
            listOrder.put(c.getDishName(), Integer.valueOf(c.getDishQuantity()));
        }
        CollectionReference tableCollection = db.collection("My_Orders");
        DocumentReference documentReference=tableCollection.document(String.valueOf(id));
        Map<String,Object> tabledata=new HashMap<>();
        tabledata.put("tablenumber",tableNo);
        tabledata.put("preferences",listOrder);
        tabledata.put("Id",id);
        tabledata.put("Total Amount",totalAmount);
        tabledata.put("Restaurant",restaurant);
        tabledata.put("email",mail);

        documentReference.set(tabledata)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data added successfully
                       // Toast.makeText(Checkout.this, "added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(Checkout.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });


    }
    private void sendDataToAdmin(int id) {
        HashMap<String,Integer> listOrder = new HashMap<>();
        for (CheckoutModel c:CheckoutUserAdapter.checkoutModelList){
            listOrder.put(c.getDishName(), Integer.valueOf(c.getDishQuantity()));
        }
        CollectionReference tableCollection = db.collection("Order");
        DocumentReference documentReference=tableCollection.document(String.valueOf(id));
        Map<String,Object> tabledata=new HashMap<>();
        tabledata.put("tablenumber",tableNo);
        tabledata.put("preferences",listOrder);
        tabledata.put("Id",id);
        tabledata.put("Total Amount",totalAmount);
        tabledata.put("Restaurant",restaurant);
        tabledata.put("email",mail);

        documentReference.set(tabledata)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data added successfully
                        Toast.makeText(Checkout.this, "added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(Checkout.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setRecyclerViewList(){
        Log.d("quantity map", HomeItemUserAdapter.dishList.size()+"");
        String v="1";
        for (HomeItemUserModel home: HomeItemUserAdapter.dishList ) {
            Log.d("quantity map", "is " + home.getDishName()+" "+home.getDishAmount());
            CheckoutModel checkoutModel=new CheckoutModel(home.getDishName(),home.getDishAmount(),v);
            checkoutList.add(checkoutModel);
        }
    }

    private void populateSpinner(Spinner spinner, long tablecount) {
        String[] tableitem =new String[(int) tablecount];
        for(int i=0; i<tablecount;i++)
        {
            tableitem[i] ="Table No "+(i+1);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,tableitem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public static void setTotalAmount(){
        int i=0;
        for(CheckoutModel c:CheckoutUserAdapter.checkoutModelList){
            if (!c.getDishQuantity().equals(""))
                i=i+(Integer.parseInt(c.getDishAmount())*Integer.parseInt(c.getDishQuantity()));
        }
        txtTotalAmount.setText("Total Amount : "+i);
        totalAmount=i;
    }


}