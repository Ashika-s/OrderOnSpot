package com.sas.food_order_application.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sas.food_order_application.R;
import com.sas.food_order_application.admin.Categoryclass;
import com.sas.food_order_application.admin.Item_activity;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<Categoryclass> userArrayList;
   // ArrayList<ImageData> imageData;


    public CategoryAdapter(Context context, ArrayList<Categoryclass> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;

    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.category_view,parent,false);
       return new ViewHolder(view);
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        if (viewType == VIEW_TYPE_TEXT) {
//            View view = inflater.inflate(R.layout.category_view, parent, false);
//            return new ViewHolder(view);
//        } else if (viewType == VIEW_TYPE_IMAGE) {
//            View view = inflater.inflate(R.layout.category_view, parent, false);
//            return new ViewHolder(view);
//        }
//        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
      Categoryclass category=userArrayList.get(position);
      holder.categoryy.setText(category.getCategory());
//
//        Glide.with(holder.itemView.getContext())
//                .load(category.getImageurl())
//                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return userArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView categoryy;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageVerDish);
            categoryy=itemView.findViewById(R.id.textCategory);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inten= new Intent(context,Item_activity.class);
                    inten.putExtra("clicedString",categoryy.getText().toString());
                    context.startActivity(inten);
                }
            });
        }
    }
//    public void setTextDataList(ArrayList<Categoryclass> userArrayList) {
//        this.userArrayList = userArrayList;
//        notifyDataSetChanged();
//    }

//    public void setImageDataList(ArrayList<ImageData> imageData) {
//        this.imageData = imageData;
//        notifyDataSetChanged();
//    }
}