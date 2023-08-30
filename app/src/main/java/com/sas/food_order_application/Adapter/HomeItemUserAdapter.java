package com.sas.food_order_application.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sas.food_order_application.Model.HomeItemUserModel;
import com.sas.food_order_application.R;
import com.sas.food_order_application.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeItemUserAdapter extends RecyclerView.Adapter<HomeItemUserAdapter.ViewHolder> {
    Context context;
    List<HomeItemUserModel> list;

    public static List<HomeItemUserModel> dishList = new ArrayList<>();

    public HomeItemUserAdapter(Context context, List<HomeItemUserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_ver_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imageDish.setImageResource(R.drawable.burger);
        holder.txtDishName.setText(list.get(position).getDishName());
        holder.txtAmount.setText("Amount : "+list.get(position).getDishAmount());
    }

    public void setFilteredList(List<HomeItemUserModel> filteredList){
        this.list=filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageDish;
        TextView txtDishName,txtAmount;
        TextView txtItemCount;
        Button btnAddCart;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageDish= itemView.findViewById(R.id.imageVerDish);
            txtDishName= itemView.findViewById(R.id.txtDishName);
            txtAmount= itemView.findViewById(R.id.txtDishAmount);
            btnAddCart=itemView.findViewById(R.id.btnAddCart);
            btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    String tempName=txtDishName.getText().toString();
                    Log.d("quantity","is "+tempName+"position "+position);
                    if(position!=RecyclerView.NO_POSITION) {
                        if (!dishList.contains(list.get(position))){
                             dishList.add(list.get(position));
                        }else{
                            Toast.makeText(itemView.getContext(), "Item Already Added",Toast.LENGTH_SHORT).show();
                        }
                        Log.d("quantity map", "is " + dishList + "value is " + dishList.size());
                        HomeFragment.setVisibility(dishList.size());
                    }
                    HomeFragment.txtItemsAdded.setText(+dishList.size()+" ITEM ADDED");
                }
            });
        }
    }
}