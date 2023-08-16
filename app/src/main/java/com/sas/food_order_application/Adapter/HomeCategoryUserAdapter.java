package com.sas.food_order_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sas.food_order_application.Model.HomeCategoryUserModel;
import com.sas.food_order_application.R;

import java.util.List;

public class HomeCategoryUserAdapter extends RecyclerView.Adapter<HomeCategoryUserAdapter.ViewHolder>  {

    Context context;
    List<HomeCategoryUserModel> homeCategoryModels;
    RecyclerView homeItemRec ;
//    private ItemClickListener itemClickListener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder=new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_hor_item,parent,false));
        return viewHolder;
    }

    public HomeCategoryUserAdapter(Context context, List<HomeCategoryUserModel> homeCategoryModels) {
        this.context = context;
        this.homeCategoryModels = homeCategoryModels;
//        this.itemClickListener=itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeCategoryUserModel homeCategorymodel =homeCategoryModels.get(position);
        holder.bind(homeCategorymodel.getTextView());
    }


    @Override
    public int getItemCount() {
        return homeCategoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.textCategory);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    new HomeFragment().itemUpdate(category.getText().toString());
                }
            });
        }

        public void bind(String clickedCategory) {
            category.setText(clickedCategory);
        }
    }

    public interface ItemClickListener {
        public void onItemClickListener(String item);
    }

}
