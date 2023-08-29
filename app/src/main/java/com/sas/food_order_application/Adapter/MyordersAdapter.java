package com.sas.food_order_application.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.sas.food_order_application.R;
import com.sas.food_order_application.user.Feedback;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
public class MyordersAdapter extends RecyclerView.Adapter<MyordersAdapter.ViewHolder> {

    private List<DocumentSnapshot> tableDataList;
    Context context;

    public MyordersAdapter(List<DocumentSnapshot> tableDataList, Context context) {
        this.context = context;
        this.tableDataList = tableDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorderview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DocumentSnapshot documentSnapshot = tableDataList.get(position);
        String restname =String.valueOf(documentSnapshot.get("Restaurant"));
        String ID = String.valueOf(documentSnapshot.get("Id"));
        String tableNumber = String.valueOf(documentSnapshot.get("tablenumber"));
        String amount = String.valueOf(documentSnapshot.get("Total Amount"));
        Log.d("order", "is " + amount);
        Map<String, Object> preferencesMap = (Map<String, Object>) documentSnapshot.get("preferences");
        holder.bindData(restname,ID, tableNumber, amount, preferencesMap);
        Context context = holder.itemView.getContext();
        TextView feedback = holder.itemView.findViewById(R.id.feedback);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Feedback.class);
                intent.putExtra("order_id", ID);
                intent.putExtra("restname",restname);
                intent.putExtra("list", (Serializable) preferencesMap);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return tableDataList.size(); // Return the actual size of the data list
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView feedback;
        TextView restname;
        TextView Id;
        TextView Tablenumber;
        TextView TotalAmount;
        TextView preferencesTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            feedback=itemView.findViewById(R.id.feedback);
            restname=itemView.findViewById(R.id.rstname);
            Id = itemView.findViewById(R.id.idd);
            Tablenumber = itemView.findViewById(R.id.tablenoo);
            TotalAmount = itemView.findViewById(R.id.totalAmountt);
            preferencesTextView = itemView.findViewById(R.id.preferencesTextVieww);
        }

        public void bindData(String rstname,String id, String tableNumber, String amount, Map<String, Object> preferencesMap) {
            restname.setText(rstname);
            Id.setText("ID : " + id);
            Tablenumber.setText("TABLE NUMBER : " + tableNumber);
            TotalAmount.setText("AMOUNT : " + amount);

            StringBuilder preferencesText = new StringBuilder();
            for (Map.Entry<String, Object> entry : preferencesMap.entrySet()) {
                preferencesText.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
            }
            preferencesTextView.setText(preferencesText.toString());
        }
    }
}
