package com.sas.food_order_application.user;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.sas.food_order_application.R;
import com.sas.food_order_application.Welcome;

public class orderplaced_splash extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderplaced_splash);

        lottieAnimationView=findViewById(R.id.lottiesplash);
        lottieAnimationView.animate().translationX(2000).setDuration(2000).setStartDelay(2900);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("NEW LOGIN",true);
                startActivity(intent);
                finish();
            }
        },3000);

    }
}
