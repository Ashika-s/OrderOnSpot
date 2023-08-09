package com.sas.food_order_application.ui.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sas.food_order_application.R;
import com.sas.food_order_application.User_login;
import com.sas.food_order_application.Userclass;
public class MyprofileActivity extends AppCompatActivity {
    EditText Name;
    TextView Email;
    EditText Password;
    Button Update;
    FirebaseFirestore db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
            Name = findViewById(R.id.prname);
            Email = findViewById(R.id.premail);
            Password = findViewById(R.id.prpassword);
            Update=findViewById(R.id.profilebutton);

            db = FirebaseFirestore.getInstance();
            String emaill =  User_login.emailid;

            fetchData(emaill);
            Update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateData();
                }
            });
    }

    void updateData(){
        String name = (Name).getText().toString();
        String email = (Email).getText().toString();
        String password =(Password).getText().toString();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            user.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful()){
                                   Log.d("profile", "email updated!"+email);
                               }else {
                                   Log.d("profile", "failed");
                               }
                           }
                       }
                    );
            user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d("profile", "password updated!"+password);
                    }else {
                        Log.d("profile", "failed");
                    }
                }
            });
        }
        if (email != null) {
            DocumentReference update1 = db.collection("Customer").document(email);
            update1
                    .update("name", name,
                            "email",email,
                            "password",password
                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                           Toast.makeText( MyprofileActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            Log.d("profile", "DocumentSnapshot successfully updated!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                         //   Toast.makeText(MyprofileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            Log.w("profile", "failed");
                        }
                    });
        }
    }

    void fetchData(String email){
        DocumentReference docRef = db.collection("Customer").document(email);
        Log.d("profile", "value is "+docRef);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d("profile", "value is "+document);
                    Log.d("profile", "value is "+document.exists());
                    if (document.exists()) {
                        Log.d("profile", "Successfully getting the data...");
                        Userclass userclass = document.toObject(Userclass.class);
                        Name.setText(userclass.getName());
                        Email.setText(userclass.getEmail());
                        Password.setText(userclass.getPassword());
                    } else {
                        Log.d("profile", "No such document");
                    }
                } else {
                    Log.d("profile", "get failed with ", task.getException());
                }
            }
        });
    }
}