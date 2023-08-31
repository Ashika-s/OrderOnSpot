package com.sas.food_order_application.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.sas.food_order_application.R;
import com.sas.food_order_application.SplashScreen;

public class UserRegister extends AppCompatActivity {
    Button register;
    EditText name;
    EditText email;
    EditText password;
    EditText confpassword;
    FirebaseAuth auth;
    TextView click;
    DocumentReference ref;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser=auth.getCurrentUser();
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

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        register = findViewById(R.id.registe);
        collectionReference=db.collection("users");
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confpassword = findViewById(R.id.conf);
        click=findViewById(R.id.signin);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserRegister.this, UserLogin.class);
                startActivity(intent);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namee, emaill, passwordd, confpasswordd;
                namee = String.valueOf(name.getText());
                emaill = String.valueOf(email.getText());
                passwordd = String.valueOf(password.getText());
                confpasswordd = String.valueOf(confpassword.getText());
                UserLogin.emailid=emaill;
                SharedPreferences preferences = getSharedPreferences("localEmailUser", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("KEY_EMAIL_USER", emaill);
                editor.apply();
                if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confpassword.getText().toString().isEmpty())
                {
                    confpassword.setError("Confirm password cannot be empty");
                    confpassword.requestFocus();
                    name.setError("name cannot be empty");
                    name.requestFocus();
                    email.setError("email cannot be empty");
                    email.requestFocus();
                    password.setError("password cannot be empty");
                    password.requestFocus();
                    return;
                }
                else if(!password.getText().toString().equals(confpassword.getText().toString()))
                {
                    confpassword.setError("Passwords are not matching");
                    confpassword.requestFocus();
                     return;
                }
                auth.createUserWithEmailAndPassword(emaill,passwordd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    addNewData(namee,passwordd,emaill);
                                    Toast.makeText(UserRegister.this, "succefull", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d("user register","Authentication failed");
                                }
                            }
                        });
                }
        });
    }
    void addNewData(String namee,String passwordd,String emaill){
        Userclass userclass=new Userclass();
        userclass.setName(namee);
        userclass.setPassword(passwordd);
        userclass.setEmail(emaill);
        db.collection("Customer").document(emaill).set(userclass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                SplashScreen.userType="Customer";
                setResult(1);
                Intent intent=new Intent(UserRegister.this,MainActivity.class);
                intent.putExtra("Perform Select Restaurant",true);
                intent.putExtra("NEW LOGIN",false);
                SharedPreferences preferences1= getSharedPreferences("localUserDetection", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = preferences1.edit();
                editor1.putString("KEY_USER_TYPE", "2");
                editor1.apply();
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserRegister.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}