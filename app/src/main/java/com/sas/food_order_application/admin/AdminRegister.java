package com.sas.food_order_application.admin;

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

import java.util.HashMap;
import java.util.Map;

public class AdminRegister extends AppCompatActivity {
    Button register;
    EditText Restaurantname;
    EditText Restaurantaddress;
    EditText Restaurantphno;
    EditText Ownername;
    EditText Ownerphno;
    EditText password;
    EditText confirmpassword;
    EditText email;
    TextView click;
    FirebaseAuth auth;
    CollectionReference collectionReference;
    DocumentReference ref;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String restname;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser=auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent=new Intent(AdminRegister.this, AdminMain.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register2);


        Restaurantname = findViewById(R.id.name);
        Restaurantaddress = findViewById(R.id.resadd);
        Restaurantphno = findViewById(R.id.resphno);
        Ownername = findViewById(R.id.ownername);
        Ownerphno = findViewById(R.id.ownernumber);
        password =findViewById(R.id.pass);
        confirmpassword=findViewById(R.id.confpass);
        email=findViewById(R.id.owneremail);
        collectionReference=db.collection("Admin");
        auth=FirebaseAuth.getInstance();
        register=findViewById(R.id.adminregiste);
        click=findViewById(R.id.signinadmin);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminRegister.this, AdminLogin.class);
                startActivity(intent);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Restaurantnamee,Restaurantaddresss ,Restaurantphnoo ,Ownernamee ,Ownerphnoo ,passwordd ,confirmpasswordd,emaill;
                Restaurantnamee = String.valueOf(Restaurantname.getText());
                Restaurantaddresss = String.valueOf(Restaurantaddress.getText());
                Restaurantphnoo= String.valueOf(Restaurantphno.getText());
                Ownernamee = String.valueOf(Ownername.getText());
                Ownerphnoo = String.valueOf(Ownerphno.getText());
                passwordd = String.valueOf(password.getText());
                emaill=String.valueOf(email.getText());
                confirmpasswordd = String.valueOf(confirmpassword.getText());
                AdminLogin.adminemailid=emaill;

                if (Restaurantname.getText().toString().isEmpty() || Restaurantaddress.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmpassword.getText().toString().isEmpty())
                {
                    confirmpassword.setError("Confirm password cannot be empty");
                    confirmpassword.requestFocus();
                    Ownername.setError("Owner Name cannot be empty");
                    Ownername.requestFocus();
                    email.setError("email cannot be empty");
                    email.requestFocus();
                    password.setError("password cannot be empty");
                    password.requestFocus();
                    Restaurantname.setError("Restaurant Name cannot be empty");
                    Restaurantname.requestFocus();
                    Restaurantphno.setError("Restaurant Phone number cannot be empty");
                    Restaurantphno.requestFocus();
                    Ownerphno.setError("Owner Phone NUmber cannot be empty");
                    Ownerphno.requestFocus();
                    password.setError("password cannot be empty");
                    password.requestFocus();
                    Restaurantaddress.setError("Address cannot be empty");
                    Restaurantaddress.requestFocus();
                    return;
                }
                else if(!password.getText().toString().equals(confirmpassword.getText().toString()))
                {
                    confirmpassword.setError("Passwords are not matching");
                    confirmpassword.requestFocus();
                    return;
                }
                auth.createUserWithEmailAndPassword(emaill,passwordd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    addNewData(Restaurantnamee,Restaurantaddresss,Restaurantphnoo,Ownernamee,Ownerphnoo,passwordd,emaill);
                                    Toast.makeText(AdminRegister.this, "succefull", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d("admin register","Authentication failed");
                                }
                            }
                        });
            }
        });
    }

    void addNewData(String Restaurantnamee,String Restaurantaddresss,String Restaurantphnoo,String Ownernamee,String Ownerphnoo,String passwordd,String emaill){
        AdminRegisterClass adminRegisterClass = new AdminRegisterClass();
        adminRegisterClass.setRestorantName(Restaurantnamee);
        adminRegisterClass.setRestaurantaddress(Restaurantaddresss);
        adminRegisterClass.setRestaurantphno(Restaurantphnoo);
        adminRegisterClass.setOwnername(Ownernamee);
        adminRegisterClass.setOwnerphno(Ownerphnoo);
        adminRegisterClass.setPassword(passwordd);
        adminRegisterClass.setEmail(emaill);
        Log.d("addingData",Restaurantnamee);
        SharedPreferences preferences = getSharedPreferences("localEmailAdmin", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("KEY_EMAIL_ADMIN", emaill);
        editor.apply();
        db.collection("Admin").document(emaill).set(adminRegisterClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                restname=Restaurantnamee;
                Log.d("restname","is "+restname);
          //      progressBar.setVisibility((View.GONE));
                startActivity(new Intent(AdminRegister.this, AdminMain.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminRegister.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        Map<String,Object> map=new HashMap<>();
        map.put("Email",emaill);
        db.collection("Restaurant").document(Restaurantnamee).set(map);
    }
}
