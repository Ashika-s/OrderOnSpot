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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeItemUserAdapter extends RecyclerView.Adapter<HomeItemUserAdapter.ViewHolder> {

    Context context;
    List<HomeItemUserModel> list;
    int[] minteger = {0};
    List<QuantityButton> quantityButtonList;
    static HashMap<String,Integer> hashMap = new HashMap<>();

    public HomeItemUserAdapter(Context context, List<HomeItemUserModel> list) {
        this.context = context;
        this.list = list;
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

//        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                int value = 1;
//                String tempName=holder.txtDishName.getText().toString();
//                Log.d("quantity","is "+tempName);
//                if(position!=RecyclerView.NO_POSITION) {
//                    Log.d("quantity position", "is " + position);
//                    if (!hashMap.containsKey(tempName)) {
//                        hashMap.put(tempName, value);
//                        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
//                            String key = entry.getKey();
//                            Integer val = entry.getValue();
//                            Log.d("quantity map", "is " + entry.getKey() + "value is " + val);
//                        }
//                    }
//
//                    else
//                        {
//
//                            int currentvalue = hashMap.get(tempName);
//                            hashMap.put(tempName, currentvalue + 1);
//                            for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
//                                String key = entry.getKey();
//                                Integer val = entry.getValue();
//                                Log.d("quantity map", "is " + entry.getKey() + "value is " + val);
//                            }
//
//                        }
//
//                        holder.txtItemCount.setText(String.valueOf(hashMap.get(tempName)));
//                    Log.d("quantity map set", "is "+holder.txtItemCount.getText().toString());
//                     //   notifyItemChanged(position);
//                        notifyDataSetChanged();
//                    }
//                }
//
//
//        });
//
//        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
//            @Override
//
//            public void onClick(View v) {
//               // int pos=getAdapterPosition();
//                int value = 1;
//                 String tempName=holder.txtDishName.getText().toString();
//                if(position!=RecyclerView.NO_POSITION &&  minteger[0]>0) {
//                 if(!hashMap.containsKey(tempName)) {
//                     hashMap.put(tempName,value);
//
//                 }else
//                 {
//                     int currentvalue=hashMap.get(tempName);
//                     hashMap.put(tempName,currentvalue-1);
//
//                 }
//               // minteger[0] = Integer.parseInt(holder.txtItemCount.getText().toString());
//
//                   // minteger[0] = minteger[0] - 1;
//                    holder.txtItemCount.setText(hashMap.get(tempName));
//                  //  notifyItemChanged(position);
//                    notifyDataSetChanged();
//                }
//            }
//        });

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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageDish;
        TextView txtDishName,txtAmount;
        TextView txtItemCount;
        Button btnDecrease,btnIncrease;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageDish= itemView.findViewById(R.id.imageVerDish);
            txtDishName= itemView.findViewById(R.id.txtDishName);
            txtAmount= itemView.findViewById(R.id.txtDishAmount);
            txtItemCount=itemView.findViewById(R.id.txtinteger_number);
            btnDecrease=itemView.findViewById(R.id.btndecrease);
            btnIncrease=itemView.findViewById(R.id.btnincrease);

            btnIncrease.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int value = 1;
                    int position=getAdapterPosition();
                    String tempName=txtDishName.getText().toString();
                    Log.d("quantity","is "+tempName);
                    if(position!=RecyclerView.NO_POSITION) {
                        if (!hashMap.containsKey(tempName)) {
                            hashMap.put(tempName, value);
                            for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
                                String key = entry.getKey();
                                Integer val = entry.getValue();
                                Log.d("quantity map", "is " + entry.getKey() + "value is " + val);
                            }
                        }

                        else
                        {

                            int currentvalue = hashMap.get(tempName);
                            hashMap.put(tempName, currentvalue + 1);
                            for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
                                String key = entry.getKey();
                                Integer val = entry.getValue();
                                Log.d("quantity map", "is " + entry.getKey() + "value is " + val);
                            }

                        }

                        txtItemCount.setText(String.valueOf(hashMap.get(tempName)));
                        Log.d("quantity map set", "is "+txtItemCount.getText().toString());

                        notifyItemChanged(position);
                    }
                }


            });

            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                     int position=getAdapterPosition();
                    int value = 1;
                    String tempName=txtDishName.getText().toString();
                    Log.d("quantity","is "+tempName);
                    if(position!=RecyclerView.NO_POSITION && hashMap.get(tempName)>0) {
                        Log.d("quantity","is "+hashMap.get(tempName));

                            int currentvalue=hashMap.get(tempName);
                            hashMap.put(tempName, currentvalue - 1);
                            if (currentvalue==0){
                                hashMap.remove(tempName);
                                txtItemCount.setText(0);

                            }
                            else {
                                Log.d("quantity map set", "is "+hashMap.get(tempName));
                                txtItemCount.setText(hashMap.get(tempName));
                                for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
                                    String key = entry.getKey();
                                    Integer val = entry.getValue();
                                    Log.d("quantity map", "is " + entry.getKey() + "value is " + val);

                                }
                            }


                        Log.d("quantity map set", "is "+txtItemCount.getText().toString());
                        notifyItemChanged(position);

                    }
                }
            });


        }
    }
}