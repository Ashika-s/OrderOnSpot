package com.sas.food_order_application.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sas.food_order_application.Model.FeedbackModel;
import com.sas.food_order_application.R;

import java.util.Map;

public class Feedback extends AppCompatActivity {
Button submit;
    private RatingBar ratingBar;
    private FirebaseFirestore firestore;
    private EditText feedbackMessage;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        submit=findViewById(R.id.subbtn);

        ratingBar = findViewById(R.id.ratingBar);
        feedbackMessage = findViewById(R.id.editTextText);
        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        int orderId = Integer.parseInt(intent.getStringExtra("order_id"));
        String restname = intent.getStringExtra("restname");
        Map<String, Object> receivedMap = (Map<String, Object>) getIntent().getSerializableExtra("list");

        if (receivedMap != null) {
            Log.d("Received Map", receivedMap.toString());
        } else {
            Log.e("Received Map", "Received map is null");
        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                String message = feedbackMessage.getText().toString();
                CollectionReference feedbackCollection = firestore.collection("feedback");
                DocumentReference feedbackDocument = feedbackCollection.document(String.valueOf(orderId));
                FeedbackModel feedbackModel = new FeedbackModel(rating, message, restname, receivedMap, orderId);
                feedbackDocument.set(feedbackModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                Intent intent1=new Intent(Feedback.this,MainActivity.class);
                startActivity(intent1);
                finish();
            }

        });
    }
}