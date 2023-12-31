package com.sas.food_order_application.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sas.food_order_application.Adapter.OrderAdapter;
import com.sas.food_order_application.Adapter.OrderHistoryAdapter;
import com.sas.food_order_application.R;
import com.sas.food_order_application.Welcome;
import com.sas.food_order_application.user.UserLogin;

import java.util.ArrayList;
import java.util.List;
public class AdminHistory extends AppCompatActivity {


    DrawerLayout drawerLayout;
    ImageView imageView;

    ImageView refresh;
    LinearLayout menu,orders,profile,history,feedback,logout;
    List<DocumentSnapshot> tableDataList;
    RecyclerView recyclerView;
    OrderHistoryAdapter tableDataAdapter;
    String email= AdminLogin.adminemailid;

    private Handler handler = new Handler();
    private Runnable refreshRunnable;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_history);
//        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentuser != null){
//            SharedPreferences preferences = getSharedPreferences("localEmailAdmin", MODE_PRIVATE);
//            email = preferences.getString("KEY_EMAIL_ADMIN", "");
//            AdminLogin.adminemailid=preferences.getString("KEY_EMAIL_ADMIN", "");
//        }
        drawerLayout=findViewById(R.id.drawerlayout);
        imageView=findViewById(R.id.menu);
        refresh=findViewById(R.id.refresh);
        menu=findViewById(R.id.Menu);
        orders=findViewById(R.id.Order);
        profile=findViewById(R.id.Profile);
        history=findViewById(R.id.history);
        feedback=findViewById(R.id.receivedfeedback);
        logout=findViewById(R.id.Logout);
        recyclerView = findViewById(R.id.recyclerviewCategory3);
        getorders();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AdminHistory.this, AdminMain.class);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AdminHistory.this, AdminOrders.class);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AdminHistory.this, AdminProfile.class);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AdminHistory.this,AdminFeedback.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }

            private void showdialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminHistory.this);
                builder.setMessage("Do you want to Logout ?");
                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent intent=new Intent(AdminHistory.this, Welcome.class);
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

    private void getorders() {

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
                            CollectionReference tableDataCollection = db.collection("My_Orders");

                            tableDataCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        String restaurantName = documentSnapshot.getString("Restaurant");
                                        boolean check =restaurantName.equals(userRestaurantName);
                                        Log.d("orders","is "+check);
                                        if(check)
                                        {
                                            Log.d("order","is "+restaurantName+" "+userRestaurantName);
                                            tableDataList.add(documentSnapshot);
                                            tableDataAdapter=new OrderHistoryAdapter(tableDataList,getApplicationContext());
                                            recyclerView.setHasFixedSize(true);
                                            recyclerView.setNestedScrollingEnabled(false);
                                            recyclerView.setAdapter(tableDataAdapter);
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(refreshRunnable);
    }
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(this, Welcome.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
//        finish();
//    }
}