package com.sas.food_order_application.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sas.food_order_application.Adapter.OrderAdapter;
import com.sas.food_order_application.R;
import com.sas.food_order_application.Welcome;
import com.sas.food_order_application.admin.AdminLogin;
import com.sas.food_order_application.admin.AdminMain;

import java.util.ArrayList;
import java.util.List;

public class AdminOrders extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView imageView;
    LinearLayout menu,orders,profile,logout;
    List<DocumentSnapshot> tableDataList;

    RecyclerView recyclerView;
    OrderAdapter tableDataAdapter;
    String email= AdminLogin.adminemailid;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        drawerLayout=findViewById(R.id.drawerlayout);
        imageView=findViewById(R.id.menu);
        menu=findViewById(R.id.Menu);
        orders=findViewById(R.id.Order);
        profile=findViewById(R.id.Profile);
        logout=findViewById(R.id.Logout);

        recyclerView = findViewById(R.id.recyclerviewCategory);
        tableDataList=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Admin")
                .document(email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String userRestaurantName = documentSnapshot.getString("restorantName");
//                            Log.d("order","is "+userRestaurantName);
                            //   setupRecyclerView(userRestaurantName);

                            CollectionReference tableDataCollection = db.collection("Order");

                            tableDataCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        String restaurantName = documentSnapshot.getString("Restaurant");
//                                        Log.d("order","is "+restaurantName);
                                        boolean check =restaurantName.equals(userRestaurantName);
                                        Log.d("orders","is "+check);
                                        if(check)
                                        {
                                            Log.d("order","is "+restaurantName+" "+userRestaurantName);
                                            tableDataList.add(documentSnapshot);
                                            tableDataAdapter = new OrderAdapter(tableDataList);
//                                            tableDataAdapter.notifyDataSetChanged();
                                            recyclerView.setAdapter(tableDataAdapter);

                                        }
                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure
                                }
                            });
                        }
                    }

                });


        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AdminOrders.this, AdminMain.class);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AdminOrders.this, AdminProfile.class);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }

            private void showdialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminOrders.this);
                builder.setMessage("Do you want to Logout ?");
                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent intent=new Intent(AdminOrders.this, Welcome.class);
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

//    private void setupRecyclerView(String userRestaurantName) {
//
//
//    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity){
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