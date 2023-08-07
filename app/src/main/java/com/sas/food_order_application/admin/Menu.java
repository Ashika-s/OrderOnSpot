package com.sas.food_order_application.admin;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sas.food_order_application.Adapter.Category_Adapter;
import com.sas.food_order_application.Model.Category;
import com.sas.food_order_application.R;

import java.util.ArrayList;
import java.util.List;

public class Menu extends Fragment {

    View view;
    FloatingActionButton floatingActionButton;

    // Firebase variables
    private DatabaseReference databaseReference;
    private static final int REQUEST_IMAGE_PICK = 1;
    private Uri selectedImageUri;

    RecyclerView categoryRec;
    List<Category> categoryList=new ArrayList<>();;
    Category_Adapter category_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);

// Initialize Firebase
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("menuEntries");

// Set OnClickListener for the FloatingActionButton
        floatingActionButton = view.findViewById(R.id.flt1);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.flt1) {
// Handle the click event for FloatingActionButton
                    showAlertDialog();
                }
            }
        });
       categoryRec = view.findViewById(R.id.recyclerviewCategory);


        categoryRec.setLayoutManager(new LinearLayoutManager(getActivity()));

// Initialize the list to store data retrieved from Firebase
        List<Category> menuEntries = new ArrayList<>();

// Firebase query to retrieve data from the "menuEntries" node
//        DatabaseReference menuEntriesRef = FirebaseDatabase.getInstance().getReference().child("menuEntries");
//        menuEntriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot entrySnapshot : dataSnapshot.getChildren()) {
//                    Category menuEntry = entrySnapshot.getValue(Category.class);
//                    if (menuEntry != null) {
//                        menuEntries.add(menuEntry);
//                    }
//                }
//// After fetching data, create and set the adapter for the RecyclerView
////                MyAdapter adapter = new MyAdapter(getContext(), menuEntries);
////                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//// Handle error if data fetching fails
//                Toast.makeText(getContext(), "Error fetching data from Firebase", Toast.LENGTH_SHORT).show();
//            }
//        });


        return view;
    }

    // Function to show input dialog
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_layout, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        Button btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);

// Set up the button click listener for image selection
        btnSelectImage.setOnClickListener(view -> {
            openGallery();
        });

// Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String userInputName = editTextName.getText().toString();

// Process the selectedImageUri if needed
            if (!userInputName.isEmpty() && selectedImageUri != null) {
                //convert the uri to a string for storing in firebase
                String imageUriString=selectedImageUri.toString();
// Store the data in Firebase
                String entryKey = databaseReference.push().getKey();
//                Category menuEntry = new Category(userInputName, selectedImageUri);
//                databaseReference.child(entryKey).setValue(menuEntry);

// Clear the selectedImageUri for the next input
                selectedImageUri = null;

// Show a Toast message to indicate the data has been added
                Toast.makeText(getContext(), "Data added to Firebase.", Toast.LENGTH_SHORT).show();
            } else {
// Handle the case when the name input or image is empty
                Toast.makeText(getContext(), "Please enter a name and select an image.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Function to open the gallery and select an image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    // Handle the result when an image is selected from the gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
        }
    }
}