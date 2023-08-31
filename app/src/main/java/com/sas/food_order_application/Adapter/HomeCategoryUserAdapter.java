package com.sas.food_order_application.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sas.food_order_application.Model.HomeCategoryUserModel;
import com.sas.food_order_application.Model.HomeItemUserModel;
import com.sas.food_order_application.R;
import com.sas.food_order_application.admin.Categoryclass;
import com.sas.food_order_application.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeCategoryUserAdapter extends RecyclerView.Adapter<HomeCategoryUserAdapter.ViewHolder>  {
    Context context;
    List<HomeCategoryUserModel> homeCategoryModels;
    List<Categoryclass> categoryclassList;
    List<HomeItemUserModel> homeItemUserModelList=new ArrayList<>();
    HomeItemUserAdapter homeItemUserAdapter;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder=new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_hor_item,parent,false));
        return viewHolder;
    }

    public HomeCategoryUserAdapter(Context context, List<HomeCategoryUserModel> homeCategoryModels, List<Categoryclass> categoryclassList) {
        this.context = context;
        this.homeCategoryModels = homeCategoryModels;
        this.categoryclassList=categoryclassList;
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
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        homeItemUserModelList.clear();
                        getUpdatedSecondItems(category.getText().toString());
                        for (HomeItemUserModel f : homeItemUserModelList) {
                            Log.d("Items", " " + f.getDishName());
                        }
                        HomeFragment.getUpdateAdapter(homeItemUserModelList);
                    }
                }
            });
        }

        public void bind(String clickedCategory) {
            category.setText(clickedCategory);
        }
    }

    public void getUpdatedSecondItems(String clickedItem) {
    String clickedCategory= clickedItem;
    Log.d("clicked elements"," "+clickedCategory);
    Log.d("selected rest", HomeFragment.selectedRest);
    for (Categoryclass category:categoryclassList){
        if(category.getCategory().equals(clickedCategory)){
            HomeItemUserModel homeItemUserModel = new HomeItemUserModel(category.getItem(),category.getAmount(),String.valueOf(0), category.getType());
            homeItemUserModelList.add(homeItemUserModel);
        }
    }
     Log.d("Item List2"," "+homeItemUserModelList.toString());
    }
}