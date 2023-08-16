package com.sas.food_order_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sas.food_order_application.Model.CheckoutModel;
import com.sas.food_order_application.R;

import java.util.List;

public class CheckoutUserAdapter extends RecyclerView.Adapter<CheckoutUserAdapter.ViewHolder> {

    Context context;
    List<CheckoutModel> checkoutModelList;

    public CheckoutUserAdapter(Context context, List<CheckoutModel> list) {
        this.context = context;
        this.checkoutModelList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_checkout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtDishName.setText(checkoutModelList.get(position).getDishName());
        holder.txtItemCount.setText(checkoutModelList.get(position).getDishQuantity());
        holder.txtDishAmount.setText(checkoutModelList.get(position).getDishAmount());

    }

    @Override
    public int getItemCount() {
        return checkoutModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDishName,txtDishAmount,txtItemCount;
        Button btnDecrease,btnIncrease;
        ImageView imageDish;

        int minteger=0;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDishAmount=itemView.findViewById(R.id.txtCheckoutDishAmount);
            txtDishName=itemView.findViewById(R.id.txtCheckoutDishAmount);
            btnDecrease=itemView.findViewById(R.id.decrease);
            btnIncrease=itemView.findViewById(R.id.increase);
            imageDish=itemView.findViewById(R.id.imageCheckoutDish);
            txtItemCount=itemView.findViewById(R.id.integer_number);

            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    minteger+=1;
                    txtItemCount.setText(minteger);
                }
            });
            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(minteger>0) {
                        minteger--;
                        txtItemCount.setText(minteger);
                    }

                    //need to add function which delete item when count is zero
                }
            });



        }
    }
}
