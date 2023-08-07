package com.sas.food_order_application.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sas.food_order_application.Adapter.Category_Adapter;
import com.sas.food_order_application.Model.Category;
import com.sas.food_order_application.R;

//import com.google.cloud.firestore.CollectionGroupQuery;
//import com.google.cloud.firestore.Firestore;
//import com.google.cloud.firestore.FirestoreOptions;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Edit_Categories extends AppCompatActivity {


    TextView item;
    TextView category;
    TextView type;
    TextView amount;
    Button add;

    String restname=Admin_register.restname;
FirebaseFirestore db=FirebaseFirestore.getInstance();
DocumentReference documentReference;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_categories);


        item=findViewById(R.id.items);
        category=findViewById(R.id.category);
        type=findViewById(R.id.type);
        amount=findViewById(R.id.amount);
        add=findViewById(R.id.addbtn);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemm,categoryy,typee,amountt;
                itemm = String.valueOf(item.getText());
                categoryy=String.valueOf(category.getText());
                typee = String.valueOf(type.getText());
                amountt = String.valueOf(amount.getText());

                addcategoryies(itemm,categoryy,typee,amountt);

                }
        });





    }

    private void addcategoryies(String item,String category,String type,String amount) {

        Categoryclass categoryclass=new Categoryclass();
        categoryclass.setItem(item);
        categoryclass.setCategory(category);
        categoryclass.setType(type);
        categoryclass.setAmount(amount);




        db.collection("Restaurant").document(restname).collection(type).document(item).set(categoryclass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Edit_Categories.this,"Item Added",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Edit_Categories.this,"Failed to add item",Toast.LENGTH_SHORT).show();
            }
        });







    }
}