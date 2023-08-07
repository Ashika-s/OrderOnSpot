package com.sas.food_order_application.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sas.food_order_application.MainActivity;
import com.sas.food_order_application.R;
import com.sas.food_order_application.user_register;

public class Admin_register extends AppCompatActivity {
    Button register;
    EditText Restaurantname;
    EditText Restaurantaddress;
    EditText Restaurantphno;
    EditText Ownername;
    EditText Ownerphno;
    EditText password;
    EditText confirmpassword;
    EditText email;
    FirebaseAuth auth;
    CollectionReference collectionReference;
 //   TextView click;

    DocumentReference ref;
    ProgressBar progressBar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static String restname;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser=auth.getCurrentUser();
        // String firebaseUser=ref.getId();
        if (currentUser != null) {
            Intent intent=new Intent(Admin_register.this,Admin_Main.class);
            startActivity(intent);
            finish();

        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register2);


        register = findViewById(R.id.registe);
        Restaurantname = findViewById(R.id.name);
        Restaurantaddress = findViewById(R.id.resadd);
        Restaurantphno = findViewById(R.id.resphno);
        Ownername = findViewById(R.id.ownername);
        Ownerphno = findViewById(R.id.ownernumber);
        password =findViewById(R.id.pass);
        confirmpassword=findViewById(R.id.confpass);
        email=findViewById(R.id.owneremail);

        collectionReference=db.collection("Admin");
        auth=FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progress);
        register=findViewById(R.id.registe);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Restaurantnamee,Restaurantaddresss ,Restaurantphnoo ,Ownernamee ,Ownerphnoo ,passwordd ,confirmpasswordd,emaill;
                Restaurantnamee = String.valueOf(Restaurantname.getText());
                Restaurantaddresss = String.valueOf(Restaurantaddress.getText());
                Restaurantphnoo= String.valueOf(Restaurantphno.getText());
                Ownernamee = String.valueOf(Ownername.getText());
                Ownerphnoo = String.valueOf(Ownerphno.getText());
                passwordd = String.valueOf(password.getText());
                emaill=String.valueOf(email.getText());
                confirmpasswordd = String.valueOf(confirmpassword.getText());
                progressBar.setVisibility(View.VISIBLE);



                if (Restaurantname.getText().toString().isEmpty() || Restaurantaddress.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmpassword.getText().toString().isEmpty())
                {
                    Toast.makeText(Admin_register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!password.getText().toString().equals(confirmpassword.getText().toString()))
                {
                    Toast.makeText(Admin_register.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                }
//                else if(!password.getText().toString().equals(confirmpassword.getText().toString()))
//                {
//
////                    confirmpassword.setError("Passwords are not matching");
////                    confirmpassword.requestFocus();
////                    return;
//                }
//                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emaill).matches()) {
//                    email.setError("Enter a valid email address");
//                    email.requestFocus();
//                    return;
//                }
                auth.createUserWithEmailAndPassword(emaill,passwordd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    addNewData(Restaurantnamee,Restaurantaddresss,Restaurantphnoo,Ownernamee,Ownerphnoo,passwordd,emaill,confirmpasswordd);
                                    Toast.makeText(Admin_register.this, "succefull", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Admin_register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });


    }
    void addNewData(String Restaurantnamee,String Restaurantaddresss,String Restaurantphnoo,String Ownernamee,String Ownerphnoo,String passwordd,String emaill,String confirmpasswordd){
        AdminRegisterClass adminRegisterClass = new AdminRegisterClass();
        adminRegisterClass.setRestorantName(Restaurantnamee);
        adminRegisterClass.setRestaurantaddress(Restaurantaddresss);
        adminRegisterClass.setRestaurantphno(Restaurantphnoo);
        adminRegisterClass.setOwnername(Ownernamee);
        adminRegisterClass.setOwnerphno(Ownerphnoo);
        adminRegisterClass.setPassword(passwordd);
        adminRegisterClass.setEmail(emaill);
        adminRegisterClass.setConfirmpassword(confirmpasswordd);
        //db.collection("res").document("res name").collection("cat").c

        db.collection("Admin").document(emaill).set(adminRegisterClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                restname=Restaurantnamee;

                Log.d("restname","is "+restname);

                progressBar.setVisibility((View.GONE));
                startActivity(new Intent(Admin_register.this,Admin_login.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Admin_register.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });




//        db.collection("Restaurant").document(Restaurantnamee).collection("Non Veg").
//                set(adminRegisterClass).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                progressBar.setVisibility((View.GONE));
//                startActivity(new Intent(Admin_register.this,Admin_login.class));
//                finish();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Admin_register.this, "Failed", Toast.LENGTH_SHORT).show();
//            }
//        });




        // db.collection("Restaurant").document("Ken Restaurant").
    }
}
      //  collectionReference.document("adminid").set(adminRegisterClass);
//        db.collection("Admin").add(adminRegisterClass).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        progressBar.setVisibility(View.GONE);
//                        startActivity(new Intent(Admin_register.this, Admin_Main.class));
//                        finish();
//                        Toast.makeText(Admin_register.this, "Succefully Registerd", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(Admin_register.this, "Failed", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//}

