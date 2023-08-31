package com.sas.food_order_application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sas.food_order_application.admin.AdminLogin;
import com.sas.food_order_application.admin.AdminMain;
import com.sas.food_order_application.admin.AdminOrders;
import com.sas.food_order_application.user.MainActivity;
import com.sas.food_order_application.user.UserLogin;

public class Welcome extends AppCompatActivity {
CardView admin;
CardView customer;

    private final int Request_Code_Order=1;

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
                    Log.d("Login Type",""+currentuser.getEmail());
                    Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("Perform Select Restaurant",false);
                    intent.putExtra("NEW LOGIN",true);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(getApplicationContext(), UserLogin.class);
                    startActivityForResult(intent,Request_Code_Order);
                }

            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentuser != null) {
                    startActivity(new Intent(getApplicationContext(), AdminOrders.class));
                } else {
                    Intent intent = new Intent(getApplicationContext(), AdminLogin.class);
                    startActivityForResult(intent,Request_Code_Order);
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Request_Code_Order){
            if(resultCode==1){
               finish();
            }
        }
    }
}