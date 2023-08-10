package com.sas.food_order_application.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.sas.food_order_application.R;

public class AdminLogin extends AppCompatActivity {
    Button login;
    TextView signup;
    ProgressBar progressBar;
    EditText email;
    EditText password;
    FirebaseAuth auth;
    public static String adminemailid;
    public static String res;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser=auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent=new Intent(AdminLogin.this, AdminMain.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login2);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        email=findViewById(R.id.adminemail);
        password=findViewById(R.id.adminpassword);
        login=findViewById(R.id.adminregiste);
        signup=findViewById(R.id.adminsign);
      //  progressBar=findViewById(R.id.progress);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminLogin.this, AdminRegister.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emaill, passwordd;
                emaill = String.valueOf(email.getText());
                passwordd = String.valueOf(password.getText());
               // progressBar.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(emaill) || TextUtils.isEmpty(passwordd)) {
                    email.setError("email cannot be empty");
                    email.requestFocus();
                    password.setError("password cannot be empty");
                    password.requestFocus();
                    return;
                }
                auth.signInWithEmailAndPassword(emaill,passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(
                                            @NonNull Task<AuthResult> task){
                                        if (task.isSuccessful()) {
                                           // progressBar.setVisibility(View.GONE);
                                            Log.d("user login","Login successful!!");
                                            adminemailid = emaill;
                                            Intent intent = new Intent(AdminLogin.this, AdminMain.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                 }
        });
    }
}
