package com.sas.food_order_application;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    public static String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        lottieAnimationView=findViewById(R.id.lottie);
        lottieAnimationView.animate().translationX(2000).setDuration(2000).setStartDelay(2900);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getApplicationContext(),Welcome.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }
}