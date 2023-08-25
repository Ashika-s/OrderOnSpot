package com.sas.food_order_application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sas.food_order_application.R;
import com.sas.food_order_application.admin.AdminMain;
import com.sas.food_order_application.admin.Categoryclass;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    Context context;
    List<Categoryclass> arrayList;

    public ItemAdapter(Context context, List<Categoryclass> userArrayList) {
        this.context = context;
        this.arrayList = userArrayList;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Categoryclass category=arrayList.get(position);
        holder.Amount.setText("Amount: "+category.getAmount());
        holder.ItemName.setText(category.getItem());
        //holder.imageView.setImageResource(category.getImage());
        holder.Type.setText("Type: "+ category.getType());

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                deleteItem(position);
//                return true;
//            }
//        });
    }

//    private void deleteItem(int position) {
//        if(arrayList.remove(position).getType()=="Veg")
//            FirebaseFirestore.getInstance().collection("Restaurant").document(AdminMain.rest).
//                collection("Veg").document(arrayList.get(position).getItem()).delete();
//        else
//            FirebaseFirestore.getInstance().collection("Restaurant").document(AdminMain.rest).
//                    collection("Non-Veg").document(arrayList.get(position).getItem()).delete();
//        arrayList.remove(position);
//    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView ItemName;
        TextView Amount;
        TextView Type;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemName=itemView.findViewById(R.id.ItemName);
            Amount=itemView.findViewById(R.id.Amount);
            Type=itemView.findViewById(R.id.Type);
           // imageView=itemView.findViewById(R.id.Type);
        }
    }
}