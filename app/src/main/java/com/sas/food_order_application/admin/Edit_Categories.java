package com.sas.food_order_application.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sas.food_order_application.R;

//import com.google.cloud.firestore.CollectionGroupQuery;
//import com.google.cloud.firestore.Firestore;
//import com.google.cloud.firestore.FirestoreOptions;


public class Edit_Categories extends AppCompatActivity {


    TextView item;
    TextView category;
    TextView type;
    TextView amount;
    Button add;

    String restname= AdminRegister.restname;
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
                startActivity(new Intent(Edit_Categories.this, AdminMain.class));
                }
        });
    }
    @Override
    public void onBackPressed() {
        // Create an intent to navigate to MainActivity
        Intent intent = new Intent(this, AdminMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        // Finish the current activity (optional)
        finish();
    }

    private void addcategoryies(String item,String category,String type,String amount) {
        Categoryclass categoryclass=new Categoryclass();
        categoryclass.setItem(item);
        categoryclass.setCategory(category);
        categoryclass.setType(type);
        categoryclass.setAmount(amount);
        db.collection("Admin").document(AdminLogin.adminemailid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        AdminRegisterClass adminRegisterClass = document.toObject(AdminRegisterClass.class);
                        String rest = adminRegisterClass.getRestorantName();
                        db.collection("Restaurant").document(rest).collection(type).document(item).set(categoryclass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Edit_Categories.this, "Item Added", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Edit_Categories.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}