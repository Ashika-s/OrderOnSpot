package com.sas.food_order_application.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sas.food_order_application.Model.Category;
import com.sas.food_order_application.R;
import com.sas.food_order_application.admin.AdminRegisterClass;
import com.sas.food_order_application.admin.Admin_Main;
import com.sas.food_order_application.admin.Admin_login;
import com.sas.food_order_application.admin.Categoryclass;
import com.sas.food_order_application.admin.Item_activity;
import java.util.ArrayList;
import java.util.List;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.ViewHolder> {
    Context context;
    ArrayList<Categoryclass> userArrayList;

    public Category_Adapter(Context context, ArrayList<Categoryclass> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public Category_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.category_view,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Category_Adapter.ViewHolder holder, int position) {
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