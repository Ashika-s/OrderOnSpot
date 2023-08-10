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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.sas.food_order_application.Adapter.CategoryAdapter;
import com.sas.food_order_application.Model.Category;
import com.sas.food_order_application.R;
import java.util.ArrayList;
import java.util.List;

public class Menu extends Fragment {
    View view;
    FloatingActionButton floatingActionButton;
    private DatabaseReference databaseReference;
    private static final int REQUEST_IMAGE_PICK = 1;
    private Uri selectedImageUri;
    RecyclerView categoryRec;
    List<Category> categoryList=new ArrayList<>();;
    CategoryAdapter category_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);

        floatingActionButton = view.findViewById(R.id.flt1);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.flt1) {
                    showAlertDialog();
                }
            }
        });
       categoryRec = view.findViewById(R.id.recyclerviewCategory);
        categoryRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Category> menuEntries = new ArrayList<>();
        return view;
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_layout, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        Button btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);

        btnSelectImage.setOnClickListener(view -> {
            openGallery();
        });
        builder.setPositiveButton("OK", (dialog, which) -> {
            String userInputName = editTextName.getText().toString();
            if (!userInputName.isEmpty() && selectedImageUri != null) {
                String imageUriString=selectedImageUri.toString();
                String entryKey = databaseReference.push().getKey();
                selectedImageUri = null;
                Toast.makeText(getContext(), "Data added to Firebase.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please enter a name and select an image.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
        }
    }
}