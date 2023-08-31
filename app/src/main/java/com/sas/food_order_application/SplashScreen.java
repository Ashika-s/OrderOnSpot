package com.sas.food_order_application;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sas.food_order_application.admin.AdminOrders;
import com.sas.food_order_application.user.MainActivity;

public class SplashScreen extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    public static String userType;
    public static int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        lottieAnimationView=findViewById(R.id.lottie);
        lottieAnimationView.animate().translationX(2000).setDuration(2000).setStartDelay(2900);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("localUserDetection",MODE_PRIVATE);
                i= Integer.parseInt(preferences.getString("KEY_USER_TYPE", "0"));
                FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                if(i==1) {
                    if (currentuser != null) {
                        Log.d("Login Type", "" + currentuser.getEmail());
                        startActivity(new Intent(getApplicationContext(), AdminOrders.class));
                        finish();
                    }
                }
                else if (i==2) {
                    if (currentuser != null) {
                        Log.d("Login Type", "" + currentuser.getEmail());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("Perform Select Restaurant", false);
                        intent.putExtra("NEW LOGIN", true);
                        startActivity(intent);
                        finish();
                    }
                }else {

                    Intent intent = new Intent(getApplicationContext(), Welcome.class);

                    startActivity(intent);
                    finish();
                }
            }
        },3000);

    }
}