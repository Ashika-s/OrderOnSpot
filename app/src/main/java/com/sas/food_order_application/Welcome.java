package com.sas.food_order_application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sas.food_order_application.admin.AdminLogin;
import com.sas.food_order_application.admin.AdminMain;
import com.sas.food_order_application.user.MainActivity;
import com.sas.food_order_application.user.UserLogin;

public class Welcome extends AppCompatActivity {
CardView admin;
CardView customer;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        admin=findViewById(R.id.cardone);
        customer=findViewById(R.id.cardtwo);
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentuser !=null){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else {
                    Intent intent = new Intent(getApplicationContext(), UserLogin.class);
                    startActivity(intent);
                }

            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentuser != null) {
                    startActivity(new Intent(getApplicationContext(), AdminMain.class));
                } else {
                    Intent intent = new Intent(getApplicationContext(), AdminLogin.class);
                    startActivity(intent);

                }
            }
        });
    }
}