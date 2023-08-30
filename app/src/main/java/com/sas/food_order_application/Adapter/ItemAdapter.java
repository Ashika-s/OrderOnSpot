package com.sas.food_order_application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sas.food_order_application.R;
import com.sas.food_order_application.admin.AdminMain;
import com.sas.food_order_application.admin.Categoryclass;
import com.sas.food_order_application.admin.Item_activity;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    Context context;
    List<Categoryclass> arrayList;
    StorageReference storageReference;

    public ItemAdapter(Context context, List<Categoryclass> userArrayList) {
        this.context = context;
        this.arrayList = userArrayList;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Categoryclass category=arrayList.get(position);
        holder.Amount.setText("Amount: "+category.getAmount());
        holder.ItemName.setText(category.getItem());
        holder.Type.setText("Type: "+ category.getType());
        try {
            storageReference = FirebaseStorage.getInstance().getReference("images/"+category.getItem()+".jpg");
            File localfile1 = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localfile1)
                    .addOnSuccessListener(taskSnapshot1 -> {
                        Bitmap bitmap1 = BitmapFactory.decodeFile(localfile1.getAbsolutePath());
                        holder.imageView.setImageBitmap(bitmap1);
                    }).addOnFailureListener(e -> Toast.makeText(context, "Failed to retrieve", Toast.LENGTH_SHORT).show());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView ItemName;
        TextView Amount;
        TextView Type;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemName=itemView.findViewById(R.id.ItemName);
            Amount=itemView.findViewById(R.id.Amount);
            Type=itemView.findViewById(R.id.Type);
            imageView=itemView.findViewById(R.id.imageVerDish);
        }
    }
}