package com.sas.food_order_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sas.food_order_application.admin.AdminLogin;
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
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), UserLogin.class);
                startActivity(intent);
                finish();
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), AdminLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}