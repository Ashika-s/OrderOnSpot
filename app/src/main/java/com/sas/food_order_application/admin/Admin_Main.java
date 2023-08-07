package com.sas.food_order_application.admin;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    LinearLayout menu,orders,profile,logout;


    FirebaseFirestore db=FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    CollectionReference collectionReference;
    List<Category> categoryList=new ArrayList<>();

    RecyclerView recyclerViewCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main2);

        drawerLayout=findViewById(R.id.drawerlayout);
        imageView=findViewById(R.id.menu);
        menu=findViewById(R.id.Menu);
        orders=findViewById(R.id.Order);
        profile=findViewById(R.id.Profile);
        logout=findViewById(R.id.Logout);

        floatingActionButton=findViewById(R.id.flt1);
        String restname1= Admin_login.res;
        Log.d("main","is "+restname1);
//        collectionReference=db.collection("Restaurant").document("Ashika Restaurant").collection("Veg");
//
//        Query categoryQuery= collectionReference.whereEqualTo("category","Chinese");
//        categoryQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()) {
//                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
//                        //Category category1=new Category(documentSnapshot.getString("category"));
//                        Categoryclass categoryclass = documentSnapshot.toObject(Categoryclass.class);
//                        if (!categoryList.contains(categoryclass)) {
//                            categoryList.add(categoryclass);
//                            Log.d("Man","line 80 "+categoryclass);
//                           // Log.d("category","list is"+categoryList);
//                        }
//                    }
//                }
//            }
//        });

//        CollectionReference collectionReference1=db.collection("Restaurant").document(restname1).collection("Non-Veg");
//
//        collectionReference1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()) {
//                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
//                        Category category1=new Category(documentSnapshot.getString("category"));
//                        if (!categoryList.contains(category1)) {
//                            categoryList.add();
//                        }
//                    }
//                }
//            }
//        });


        categoryList.add(new Category(" Chinese "));
        categoryList.add(new Category(" Starter "));
        categoryList.add(new Category(" Chinese "));
        categoryList.add(new Category(" Chinese "));
        categoryList.add(new Category(" Chinese "));
        Category_Adapter category_adapter=new Category_Adapter(categoryList);
        recyclerViewCategories=findViewById(R.id.recyclerviewCategory);
        recyclerViewCategories.setAdapter(category_adapter);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        recyclerViewCategories.setHasFixedSize(true);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin_Main.this, Edit_Categories.class);
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
                redirectActivity(Admin_Main.this,Admin_Orders.class);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Admin_Main.this,Admin_profile.class);
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
                    Intent intent=new Intent(Admin_Main.this, Welcome.class);
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