package com.sas.food_order_application.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sas.food_order_application.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

//import com.google.cloud.firestore.CollectionGroupQuery;
//import com.google.cloud.firestore.Firestore;
//import com.google.cloud.firestore.FirestoreOptions;


public class Edit_Categories extends AppCompatActivity {

    static final int PICK_IMAGE_REQUEST=1;
    TextView item;
    TextView category;
    Spinner type;
    TextInputLayout spinnerLayout;
    boolean isInitialTouch = true;
    TextView amount;
    Button add;

    FirebaseStorage storage;
    Button selectImageButton;
    //     EditText imageNameEditText;
    ImageView uploadedImageView;
    static String selectedOption;


    String restname= AdminRegister.restname;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    FirebaseDatabase firebaseDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_categories);
        item=findViewById(R.id.items);
        category=findViewById(R.id.category);
        type=findViewById(R.id.type);
        spinnerLayout = findViewById(R.id.spinnerLayout);
        amount=findViewById(R.id.amount);
        add=findViewById(R.id.addbtn);
        selectImageButton=findViewById(R.id.selectImageButton);
        //  imageNameEditText=findViewById(R.id.imageNameEditText);
        uploadedImageView=findViewById(R.id.uploadedImageView);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.array_name,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        type.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isInitialTouch) {
                    isInitialTouch = false;
                    // Clear the selection
                    type.setSelection(0);
                }
                return false;
            }
        });



//        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedOption = parent.getItemAtPosition(position).toString();
//                if (selectedOption.equals(getString(R.string.select_option_prompt)))
//                    spinnerLayout.setError("Please select an option"); // Set error message
//                else {
//                    spinnerLayout.setError(null);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//
//            }
//        });
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfilechooser();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemm,categoryy,typee,amountt;
                itemm = String.valueOf(item.getText());
                categoryy=String.valueOf(category.getText());

                amountt = String.valueOf(amount.getText());
                type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedOption = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {


                    }
                });

                if (TextUtils.isEmpty(itemm) || TextUtils.isEmpty(categoryy) || TextUtils.isEmpty(amountt) || TextUtils.isEmpty(selectedOption)) {
                    item.setError("Item cannot be empty");
                    item.requestFocus();
                    category.setError("Category cannot be empty");
                    category.requestFocus();
                    amount.setError("Amount cannot be empty");
                    amount.requestFocus();
                    spinnerLayout.setError("Please select an option");
                    spinnerLayout.requestFocus();

                    return;
                }

                addcategoryies(itemm,categoryy,selectedOption,amountt);

                startActivity(new Intent(Edit_Categories.this, AdminMain.class));
            }
        });
    }

    private void openfilechooser() {

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            String name=category.getText().toString();
            String imageName = name+".jpg";
            displayImage((Uri.parse(String.valueOf(imageUri))),imageName);
        }
    }

    private void uploadImageToFirestore(Uri imageUri,String imageName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" +imageName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveImageToFirestore(imageUrl);
                    });
                })
                .addOnFailureListener(exception -> {
                });

    }

    private void displayImage(Uri imageUri,String imageName) {

        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            uploadedImageView.setImageBitmap(bitmap);
            uploadImageToFirestore(imageUri,imageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void saveImageToFirestore(String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("image_url", imageUrl);

        db.collection("images")
                .add(data)
                .addOnSuccessListener(documentReference -> {

                })
                .addOnFailureListener(e -> {

                });
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

    private void addcategoryies(String item,String category,String type,String amount) {
        Categoryclass categoryclass=new Categoryclass();
        categoryclass.setItem(item);
        categoryclass.setCategory(category);
        categoryclass.setType(type);
        categoryclass.setAmount(amount);
        db.collection("Admin").document(AdminLogin.adminemailid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        AdminRegisterClass adminRegisterClass = document.toObject(AdminRegisterClass.class);
                        String rest = adminRegisterClass.getRestorantName();
                        db.collection("Restaurant").document(rest).collection(type).document(item).set(categoryclass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Edit_Categories.this, "Item Added", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Edit_Categories.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}