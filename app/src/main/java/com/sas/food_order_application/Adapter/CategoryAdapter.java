package com.sas.food_order_application.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sas.food_order_application.R;
import com.sas.food_order_application.admin.Categoryclass;
import com.sas.food_order_application.admin.Item_activity;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<Categoryclass> userArrayList;

    public CategoryAdapter(Context context, ArrayList<Categoryclass> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.category_view,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
      Categoryclass category=userArrayList.get(position);
      holder.categoryy.setText(category.getCategory());
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView categoryy;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
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
}