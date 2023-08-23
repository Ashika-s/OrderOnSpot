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
//        this.homeItemUserAdapter=homeItemUserAdapter;
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
//    List<HomeItemUserModel> updatehomeItemUserModelList= new ArrayList<>();
    Log.d("selected rest", HomeFragment.selectedRest);
//    FirebaseFirestore.getInstance().collection("Restaurant")
//        .document(HomeFragment.selectedRest)
//        .collection("Veg")
//        .get()
//        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                Log.d("Firebase","OrderOnSpot");
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Categoryclass categoryclass=document.toObject(Categoryclass.class);
//                        if (clickedCategory.equals(categoryclass.getCategory())){
//                            HomeItemUserModel homeItemUserModel=new HomeItemUserModel(categoryclass.getItem(),categoryclass.getAmount());
//                            homeItemUserModelList.add(homeItemUserModel);
//                            Log.d(selectedRest ," "+homeItemUserModel.getDishName());
//                        }
//                    }
//                } else {
//                    Log.d("firestore error", "Error getting documents: ", task.getException());
//                }
//            }
//        });
//
//        FirebaseFirestore.getInstance().collection("Restaurant").document(selectedRest).collection("Veg")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if(error!=null)
//                        {
//                            Log.e("firestore error",error.getMessage());
//                            return;
//                        }
//                        Log.d("LOG ",clickedItem.getTextView());
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                Categoryclass category = dc.getDocument().toObject(Categoryclass.class);
//                                if (clickedCategory.equals(category.getCategory())) {
//                                    HomeItemUserModel homeItemUserModel = new HomeItemUserModel( category.getItem(), category.getAmount());
//                                    Log.d("checking items",""+homeItemUserModel.getDishName());
//                                    updatehomeItemUserModelList.add(homeItemUserModel);
//                                }
//                            }
//                        }
//                    }
//                });
    for (Categoryclass category:categoryclassList){
        if(category.getCategory().equals(clickedCategory)){
            HomeItemUserModel homeItemUserModel = new HomeItemUserModel(category.getItem(),category.getAmount(),String.valueOf(0));
            homeItemUserModelList.add(homeItemUserModel);
        }
    }
     Log.d("Item List2"," "+homeItemUserModelList.toString());

    }

}