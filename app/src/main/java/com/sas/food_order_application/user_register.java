package com.sas.food_order_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class user_register extends AppCompatActivity {
    Button register;
    EditText name;
    EditText email;
    EditText password;
    EditText confpassword;
    FirebaseAuth auth;
    TextView click;

    DocumentReference ref;
    ProgressBar progressBar;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser=auth.getCurrentUser();
       // String firebaseUser=ref.getId();
        if (currentUser != null) {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);


//        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        register = findViewById(R.id.registe);
        collectionReference=db.collection("users");
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
//        click = findViewById(R.id.loginnow);
        confpassword = findViewById(R.id.conf);
        progressBar = findViewById(R.id.progress);
//        click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(user_register.this, User_login.class);
//                startActivity(intent);
//                finish();
//            }
//        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String namee, emaill, passwordd, confpasswordd;
                namee = String.valueOf(name.getText());
                emaill = String.valueOf(email.getText());
                passwordd = String.valueOf(password.getText());
                confpasswordd = String.valueOf(confpassword.getText());

                if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confpassword.getText().toString().isEmpty())
                {
                    Toast.makeText(user_register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!password.getText().toString().equals(confpassword.getText().toString()))
                {
                    Toast.makeText(user_register.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
//                    confpassword.setError("Passwords are not matching");
//                    confpassword.requestFocus();
//                   return;
                }
//                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emaill).matches()) {
//                    email.setError("Enter a valid email address");
//                    email.requestFocus();
//                    return;
//                }


                auth.createUserWithEmailAndPassword(emaill,passwordd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    addNewData(namee,passwordd,emaill,confpasswordd);
                                    Toast.makeText(user_register.this, "succefull", Toast.LENGTH_SHORT).show();
                                } else {


                                    Toast.makeText(user_register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });



                }

        });
    }
    void addNewData(String namee,String passwordd,String emaill,String confpasswordd){

        Userclass userclass=new Userclass();
        userclass.setName(namee);
        userclass.setPassword(passwordd);
        userclass.setEmail(emaill);
        userclass.setConfpassword(confpasswordd);
        db.collection("Customer").document(emaill).set(userclass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressBar.setVisibility((View.GONE));
                startActivity(new Intent(user_register.this,MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(user_register.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
       // db.collection("Restaurant").document("Ken Restaurant").
    }
}