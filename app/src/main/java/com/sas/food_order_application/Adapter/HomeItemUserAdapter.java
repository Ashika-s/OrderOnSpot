package com.sas.food_order_application.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sas.food_order_application.Model.HomeItemUserModel;
import com.sas.food_order_application.Model.QuantityButton;
import com.sas.food_order_application.R;

import java.util.List;

public class HomeItemUserAdapter extends RecyclerView.Adapter<HomeItemUserAdapter.ViewHolder> {

    Context context;
    List<HomeItemUserModel> list;
    int[] minteger = {0};
    List<QuantityButton> quantityButtonList;

    public HomeItemUserAdapter(Context context, List<HomeItemUserModel> list) {
        this.context = context;
        this.list = list;
    }

    public HomeItemUserAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_ver_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imageDish.setImageResource(list.get(position).getImage());
        holder.txtDishName.setText(list.get(position).getDishName());
        holder.txtAmount.setText("Amount : "+list.get(position).getDishAmount());
        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  int pos=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION ){
                    minteger[0] = Integer.parseInt(holder.txtItemCount.getText().toString())+1;
                    Log.d("Item Count",minteger[0]+"txt"+holder.txtItemCount.getText().toString());

                    holder.txtItemCount.setText(String.valueOf(minteger[0]));
                    notifyItemChanged(position);

                }

            }
        });

        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
               // int pos=getAdapterPosition();
                minteger[0] = Integer.parseInt(holder.txtItemCount.getText().toString());
                if(position!=RecyclerView.NO_POSITION &&  minteger[0]>0) {
                    minteger[0] = minteger[0] - 1;
                    holder.txtItemCount.setText(String.valueOf(minteger[0]));
                    notifyItemChanged(position);
                }
            }
        });

//        notifyItemChanged(position);
//        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                quantityButton.countminus();
//                notifyItemChanged(position);
//
//            }
//        });
//        holder.txtItemCount.setText(quantityButtonList.get(position).getCount());

//        holder. btnIncrease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                minteger+=1;
//                holder.txtItemCount.setText(minteger);
//            }
      //  });
//        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(minteger>0) {
//                    minteger--;
//                    holder.txtItemCount.setText(minteger);
//                }
//
//                //need to add function which delete item when count is zero
//            }
//        });

    }

    public void updateData(List<HomeItemUserModel> newItems) {
        list.clear();
        list.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageDish;
        TextView txtDishName,txtAmount;
            static public TextView txtItemCount;
        Button btnDecrease,btnIncrease;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageDish= itemView.findViewById(R.id.imageVerDish);
            txtDishName= itemView.findViewById(R.id.txtDishName);
            txtAmount= itemView.findViewById(R.id.txtDishAmount);
            txtItemCount=itemView.findViewById(R.id.txtinteger_number);
            btnDecrease=itemView.findViewById(R.id.btndecrease);
            btnIncrease=itemView.findViewById(R.id.btnincrease);


        }
    }
}