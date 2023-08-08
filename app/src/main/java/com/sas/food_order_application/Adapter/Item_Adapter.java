package com.sas.food_order_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sas.food_order_application.Model.Category;
import com.sas.food_order_application.R;
import com.sas.food_order_application.admin.Categoryclass;

import java.util.ArrayList;
import java.util.List;

public class Item_Adapter extends RecyclerView.Adapter<Category_Adapter.ViewHolder> {

    Context context;
    ArrayList<Categoryclass> arrayList;

    public Item_Adapter(Context context, ArrayList<Categoryclass> userArrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Category_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_view,parent,false);
        return new Category_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Category_Adapter.ViewHolder holder, int position) {
        Categoryclass category=arrayList.get(position);

        holder.categoryy.setText(category.getCategory());

    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView ItemName;
        TextView Amount;
        TextView Type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ItemName=itemView.findViewById(R.id.ItemName);
            Amount=itemView.findViewById(R.id.Amount);
            Type=itemView.findViewById(R.id.Type);


        }
    }
}