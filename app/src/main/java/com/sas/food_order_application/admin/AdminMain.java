package com.sas.food_order_application.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sas.food_order_application.Adapter.CategoryAdapter;
import com.sas.food_order_application.R;
import com.sas.food_order_application.Welcome;

import java.util.ArrayList;
import java.util.List;

public class AdminMain extends AppCompatActivity {
    DrawerLayout drawerLayout;
    FloatingActionButton floatingActionButton;
    ImageView imageView;
    LinearLayout menu, orders, profile, logout;
    FirebaseFirestore db;
    DocumentReference documentReference;
    CollectionReference collectionReference;
    RecyclerView recyclerView;
    ArrayList<Categoryclass> userArrayList;
 //   ArrayList<ImageData> imageDataList;
    CategoryAdapter category_adapter;
    public static String adminemaill =  AdminLogin.adminemailid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main2);

        drawerLayout = findViewById(R.id.drawerlayout);
        imageView =findViewById(R.id.menu);
        menu = findViewById(R.id.Menu);
        orders = findViewById(R.id.Order);
        profile = findViewById(R.id.Profile);
        logout = findViewById(R.id.Logout);
        floatingActionButton = findViewById(R.id.flt1);
        recyclerView=findViewById(R.id.recyclerviewCategory);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userArrayList=new ArrayList<Categoryclass>();
        //imageDataList=new ArrayList<ImageData>();
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentuser != null){
            SharedPreferences preferences = getSharedPreferences("localEmailAdmin", MODE_PRIVATE);
            adminemaill = preferences.getString("KEY_EMAIL_ADMIN", "");
            AdminLogin.adminemailid=preferences.getString("KEY_EMAIL_ADMIN", "");
        }
        EventChangeListener();

        category_adapter=new CategoryAdapter(AdminMain.this,userArrayList);
        recyclerView.setAdapter(category_adapter);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMain.this, Edit_Categories.class);
                startActivity(intent);
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AdminMain.this, AdminOrders.class);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AdminMain.this, AdminProfile.class);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }

            private void showdialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminMain.this);
                builder.setMessage("Do you want to Logout ?");
                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent intent = new Intent(AdminMain.this, Welcome.class);
                    startActivity(intent);
                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {

                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void EventChangeListener() {
        Log.d("admin Email", "is " + adminemaill);

        DocumentReference docRef = db.collection("Admin").document(adminemaill);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        AdminRegisterClass adminRegisterClass = document.toObject(AdminRegisterClass.class);
                        String rest = adminRegisterClass.getRestorantName();
                        Log.d("main", "is " + rest);
                        List<String> tempCategoryList = new ArrayList<>();
                        db.collection("Restaurant").document(rest).collection("Veg")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if(error!=null)
                                        {
                                            Log.e("firestore error",error.getMessage());
                                            return;
                                        }
                                        for(DocumentChange dc : value.getDocumentChanges()){
                                            if(dc.getType()==DocumentChange.Type.ADDED){
                                                Categoryclass category= dc.getDocument().toObject(Categoryclass.class);
                                                if(!tempCategoryList.contains(category.getCategory())) {
                                                    tempCategoryList.add(category.getCategory());
                                                    userArrayList.add(dc.getDocument().toObject(Categoryclass.class));
                                                }
                                                Log.d("list"," is"+category.getCategory());
                                            }
                                            category_adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                        db.collection("Restaurant").document(rest).collection("Non-Veg")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if(error!=null)
                                        {
                                            Log.e("firestore error",error.getMessage());
                                            return;
                                        }
                                        for(DocumentChange dc : value.getDocumentChanges()){
                                            if(dc.getType()==DocumentChange.Type.ADDED){
                                                Categoryclass category= dc.getDocument().toObject(Categoryclass.class);
                                                if(!tempCategoryList.contains(category.getCategory())) {
                                                    tempCategoryList.add(category.getCategory());
                                                    userArrayList.add(dc.getDocument().toObject(Categoryclass.class));
                                                }
                                            }
                                            category_adapter.notifyDataSetChanged();
                                        }
                                    }
                                });

                    }
                }
            }
        });


//        CollectionReference imageCollectionref = db.collection("images");
//
//        imageCollectionref.get().addOnSuccessListener(queryDocumentSnapshots -> {
//            List<ImageData> imageDataList=new ArrayList<>();
//            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//                ImageData imageData=documentSnapshot.toObject(ImageData.class);
//                imageDataList.add(imageData);
//            }
//            populateRecyclerView(userArrayList, imageDataList);
//
//        }).addOnFailureListener(e -> {
//            // Handle error
//        });
    }

//    private void populateRecyclerView(ArrayList<Categoryclass> userArrayList, List<ImageData> imageDataList) {
//
//    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity,Class secondActivity){
        Intent intent = new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}


