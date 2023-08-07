package com.sas.food_order_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class User_login extends AppCompatActivity {

    EditText email;
    EditText password;
    Button Login;
    TextView signup;
    FirebaseAuth auth;
    FirebaseFirestore db;

    ProgressBar progressBar;
   public static String emailid;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
//        ref = db.collection("user").document();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Login = findViewById(R.id.registe);
        signup = findViewById(R.id.sign);
        progressBar = findViewById(R.id.progress);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(User_login.this, user_register.class);
                startActivity(intent);
                finish();
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emaill, passwordd;

                emaill = String.valueOf(email.getText());
                passwordd = String.valueOf(password.getText());


                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(emaill) && TextUtils.isEmpty(passwordd)) {
                    Toast.makeText(User_login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;

                }
                Toast.makeText(User_login.this, ""+emaill+"pass : "+passwordd, Toast.LENGTH_SHORT).show();
// signin existing user
                auth.signInWithEmailAndPassword(emaill, passwordd)
                        .addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(
                                            @NonNull Task<AuthResult> task)
                                    {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();

                                            emailid = emaill;
                                            // if sign-in is successful
                                            // intent to home activity
                                            Intent intent
                                                    = new Intent(User_login.this,
                                                    MainActivity.class);
                                            startActivity(intent);

                                        }

                                        else {

                                            Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                }

        });
    }
}
