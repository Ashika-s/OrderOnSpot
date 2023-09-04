package com.sas.food_order_application.user;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sas.food_order_application.Adapter.CategoryAdapter;
import com.sas.food_order_application.Adapter.RestaurantSelectAdapter;
import com.sas.food_order_application.R;
import com.sas.food_order_application.admin.AdminLogin;
import com.sas.food_order_application.admin.AdminMain;
import com.sas.food_order_application.admin.AdminRegisterClass;
import com.sas.food_order_application.admin.Categoryclass;

import java.util.ArrayList;
import java.util.List;


public class SelectRestaurant extends AppCompatActivity {

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_restaurant);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference restaurantsRef = db.collection("Admin");

        restaurantsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> restaurantNames = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String restaurantName = document.getString("restorantName");
                        if (restaurantName != null) {
                            restaurantNames.add(restaurantName);
                        }
                    }
                    recyclerView = findViewById(R.id.Restaurantlist);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    RestaurantSelectAdapter adapter = new RestaurantSelectAdapter(restaurantNames); // Create your custom adapter
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting restaurant names", e);
                });

    }
}