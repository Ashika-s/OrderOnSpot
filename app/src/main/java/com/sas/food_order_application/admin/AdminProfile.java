package com.sas.food_order_application.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.sas.food_order_application.Welcome;

public class AdminProfile extends AppCompatActivity {
    TextView RestaurantName;
    EditText RestaurantAddress;
    EditText Restaurantphno;
    EditText OwnerName;
    TextView OwnerEmail;
    EditText Password;
    EditText tableno;
    Button Update;
    FirebaseFirestore db;
    DrawerLayout drawerLayout;
    ImageView imageView;
    LinearLayout menu,orders,profile,logout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        drawerLayout=findViewById(R.id.drawerlayout);
        imageView=findViewById(R.id.menu);
        menu=findViewById(R.id.Menu);
        orders=findViewById(R.id.Order);
        profile=findViewById(R.id.Profile);
        logout=findViewById(R.id.Logout);
        RestaurantName = findViewById(R.id.prresname);
        RestaurantAddress = findViewById(R.id.prresadd);
        Restaurantphno = findViewById(R.id.prresphno);
        OwnerName = findViewById(R.id.ownname);
        OwnerEmail = findViewById(R.id.ownemail);
        Password=findViewById(R.id.ownpass);
        tableno=findViewById(R.id.table);
        Update=findViewById(R.id.profilebutton);

        db = FirebaseFirestore.getInstance();
        String emaill =  AdminLogin.adminemailid;

        fetchData(emaill);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateData();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AdminProfile.this, AdminMain.class);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AdminProfile.this, AdminOrders.class);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }

            private void showdialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminProfile.this);
                builder.setMessage("Do you want to Logout ?");
                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent intent=new Intent(AdminProfile.this, Welcome.class);
                    startActivity(intent);
                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    void updateData(){
        String restaurantName = (RestaurantName).getText().toString();
        String restaurantAddress = (RestaurantAddress).getText().toString();
        String restaurantphno =(Restaurantphno).getText().toString();
        String ownername = (OwnerName).getText().toString();
        String ownerEmail = (OwnerEmail).getText().toString();
        String password =(Password).getText().toString();
        int tableNo= Integer.parseInt((tableno).getText().toString());
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            user.updateEmail(ownerEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if(task.isSuccessful()){
                                                       Log.d("profile", "email updated!"+ownerEmail);
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
                    }
                    else
                    {
                        Log.d("profile", "failed");
                    }
                }
            });
        }
        if (ownerEmail != null) {
            DocumentReference update1 = db.collection("Admin").document(ownerEmail);
            update1
                    .update("restorantName", restaurantName,
                            "restaurantaddress",restaurantAddress,
                            "restaurantphno",restaurantphno,
                            "ownername",ownername,
                            "email",ownerEmail,
                            "password",password,
                            "Tablecount",tableNo
                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText( AdminProfile.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            Log.d("profile", "DocumentSnapshot successfully updated!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                            Log.w("profile", "Error updating document");
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        // Create an intent to navigate to MainActivity
        Intent intent = new Intent(this, AdminMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        // Finish the current activity (optional)
        finish();
    }

    void fetchData(String email){
        DocumentReference docRef = db.collection("Admin").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(AdminProfile.this, "Successfully getting the data...", Toast.LENGTH_SHORT).show();
                        AdminRegisterClass adminRegisterClass = document.toObject(AdminRegisterClass.class);
                        RestaurantName.setText(adminRegisterClass.getRestorantName());
                        RestaurantAddress.setText(adminRegisterClass.getRestaurantaddress());
                        Restaurantphno.setText(adminRegisterClass.getRestaurantphno());
                        OwnerName.setText(adminRegisterClass.getOwnername());
                        OwnerEmail.setText(adminRegisterClass.getEmail());
                        Password.setText(adminRegisterClass.getPassword());
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