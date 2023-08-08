package com.sas.food_order_application.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sas.food_order_application.Adapter.Category_Adapter;
import com.sas.food_order_application.Logout;
import com.sas.food_order_application.Model.Category;
import com.sas.food_order_application.R;
import com.sas.food_order_application.Welcome;

import java.util.ArrayList;
import java.util.List;

public class Admin_Main extends AppCompatActivity {

    DrawerLayout drawerLayout;
    FloatingActionButton floatingActionButton;
    ImageView imageView;
    LinearLayout menu, orders, profile, logout;


    FirebaseFirestore db;
    DocumentReference documentReference;
    CollectionReference collectionReference;

    RecyclerView recyclerView;
    ArrayList<Categoryclass> userArrayList;
    Category_Adapter category_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main2);

        drawerLayout = findViewById(R.id.drawerlayout);
        imageView = findViewById(R.id.menu);
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
        EventChangeListener();
        category_adapter=new Category_Adapter(Admin_Main.this,userArrayList);

        recyclerView.setAdapter(category_adapter);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Main.this, Edit_Categories.class);
                startActivity(intent);
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
                redirectActivity(Admin_Main.this, Admin_Orders.class);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Admin_Main.this, Admin_profile.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // redirectActivity(Admin_Main.this,Admin_Logout.class);
                showdialog();
            }

            private void showdialog() {

                AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Main.this);
                builder.setMessage("Do you want to Logout ?");
                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent intent = new Intent(Admin_Main.this, Welcome.class);
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


        String emaill =  Admin_login.adminemailid;
        Log.d("email", "is " + emaill);
        // String email=Admin_login.adminemailid;
        DocumentReference docRef = db.collection("Admin").document(emaill);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        AdminRegisterClass adminRegisterClass = document.toObject(AdminRegisterClass.class);
                        String rest = adminRegisterClass.getRestorantName();
                        Log.d("main", "is " + rest);


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
                                                userArrayList.add(dc.getDocument().toObject(Categoryclass.class));
                                                Categoryclass category= dc.getDocument().toObject(Categoryclass.class);
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
                                                userArrayList.add(dc.getDocument().toObject(Categoryclass.class));
                                                Categoryclass category= dc.getDocument().toObject(Categoryclass.class);
                                                Log.d("list"," is"+category.getCategory());
                                            }

                                            category_adapter.notifyDataSetChanged();
                                        }

                                    }
                                });
                    }
                }
            }
        });

    }
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
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
