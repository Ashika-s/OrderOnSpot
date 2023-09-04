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

import com.sas.food_order_application.R;
import com.sas.food_order_application.admin.AdminRegisterClass;
import com.sas.food_order_application.admin.Categoryclass;
import com.sas.food_order_application.admin.Item_activity;
import com.sas.food_order_application.ui.home.HomeFragment;
import com.sas.food_order_application.user.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class RestaurantSelectAdapter extends RecyclerView.Adapter<RestaurantSelectAdapter.ViewHolder>{
Context context;
    private List<String> restaurantNames;

    public RestaurantSelectAdapter(List<String> restaurantNames) {
        this.restaurantNames = restaurantNames;
    }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurantview,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull com.sas.food_order_application.Adapter.RestaurantSelectAdapter.ViewHolder holder, int position) {
            String restaurantName = restaurantNames.get(position);
            holder.restaurantname.setText(restaurantName);
        }

        @Override
        public int getItemCount() {
            return restaurantNames.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView restaurantname;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                restaurantname=itemView.findViewById(R.id.restaurantview);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            String selectedRestaurant = restaurantNames.get(position);
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("restaurantName", selectedRestaurant);
                            context.startActivity(intent);
                        }

                    }
                });
            }
        }
}
