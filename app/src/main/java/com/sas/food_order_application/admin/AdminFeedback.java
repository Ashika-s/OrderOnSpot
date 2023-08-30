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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sas.food_order_application.Adapter.FeedbackAdapter;
import com.sas.food_order_application.Adapter.MyordersAdapter;
import com.sas.food_order_application.Adapter.OrderAdapter;
import com.sas.food_order_application.R;
import com.sas.food_order_application.Welcome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AdminFeedback extends AppCompatActivity {
    FirebaseFirestore db;
    List<DocumentSnapshot> feedbacklist;
    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    FeedbackAdapter feedbackAdapter;
    ImageView imageView;
    String email= AdminLogin.adminemailid;
    LinearLayout menu, orders, profile,feedback, logout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_feedback);

        drawerLayout = findViewById(R.id.drawerlayout);
        imageView = findViewById(R.id.menu);
        menu = findViewById(R.id.Menu);
        orders = findViewById(R.id.Order);
        profile = findViewById(R.id.Profile);
        feedback=findViewById(R.id.receivedfeedback);
        logout = findViewById(R.id.Logout);
        db = FirebaseFirestore.getInstance();
        String emaill = AdminLogin.adminemailid;
        recyclerView = findViewById(R.id.recyclerviewCategory2);
        feedbacklist = new ArrayList<>();
        getfeedback();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AdminFeedback.this, AdminMain.class);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AdminFeedback.this, AdminOrders.class);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AdminFeedback.this, AdminProfile.class);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }

            private void showdialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminFeedback.this);
                builder.setMessage("Do you want to Logout ?");
                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent intent = new Intent(AdminFeedback.this, Welcome.class);
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

    private void getfeedback() {
        feedbacklist=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Admin")
                .document(email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String RestaurantName = documentSnapshot.getString("restorantName");

                            CollectionReference tableDataCollection = db.collection("feedback");

                            tableDataCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        String restaurantName = documentSnapshot.getString("restaurantname");
//                                        Log.d("order","is "+restaurantName);
                                        boolean check =restaurantName.equals(RestaurantName);
                                        Log.d("orders","is "+check);
                                        if(check)
                                        {
                                            Log.d("order","is "+restaurantName+" "+RestaurantName);
                                            feedbacklist.add(documentSnapshot);
                                            feedbackAdapter=new FeedbackAdapter(feedbacklist,getApplicationContext());
                                            recyclerView.setHasFixedSize(true);
                                            recyclerView.setNestedScrollingEnabled(false);
                                            recyclerView.setAdapter(feedbackAdapter);
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
    }

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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AdminMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
