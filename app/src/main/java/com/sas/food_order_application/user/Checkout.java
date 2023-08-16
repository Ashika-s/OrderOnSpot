package com.sas.food_order_application.user;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sas.food_order_application.R;

public class Checkout extends AppCompatActivity {

    FirebaseFirestore db;
    String restaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db=FirebaseFirestore.getInstance();
        Spinner spinner=findViewById(R.id.spinner);
        restaurant="sam@gmail.com";

        db.collection("Admin").document(restaurant).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        long tablecount=(long) document.get("Tablecount");
                        populateSpinner(spinner, tablecount);
                    }

                    }
            }
        });


    }

    private void populateSpinner(Spinner spinner, long tablecount) {
        String[] tableitem =new String[(int) tablecount];
        for(int i=0; i<tablecount;i++)
        {
            tableitem[i] ="Table No "+(i+1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,tableitem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}