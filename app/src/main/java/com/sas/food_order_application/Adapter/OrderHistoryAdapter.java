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

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>{

    private List<DocumentSnapshot> tableDataList;
    Context context;
    public OrderHistoryAdapter(List<DocumentSnapshot> tableDataList, Context context) {
        this.context = context;
        this.tableDataList = tableDataList;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historyview, parent, false);
        return new OrderHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        DocumentSnapshot documentSnapshot = tableDataList.get(position);
        String restname =String.valueOf(documentSnapshot.get("Restaurant"));
        String ID = String.valueOf(documentSnapshot.get("Id"));
        String tableNumber = String.valueOf(documentSnapshot.get("tablenumber"));
        String amount = String.valueOf(documentSnapshot.get("Total Amount"));
        Log.d("order", "is " + amount);
        Map<String, Object> preferencesMap = (Map<String, Object>) documentSnapshot.get("preferences");
        holder.bindData(restname,ID, tableNumber, amount, preferencesMap);
    }

    @Override
    public int getItemCount() {
        return tableDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView Id;
        TextView Tablenumber;
        TextView TotalAmount;
        TextView preferencesTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Id = itemView.findViewById(R.id.iddd);
            Tablenumber = itemView.findViewById(R.id.tablenooo);
            TotalAmount = itemView.findViewById(R.id.totalAmounttt);
            preferencesTextView = itemView.findViewById(R.id.preferencesTextViewww);
        }

        public void bindData(String rstname,String id, String tableNumber, String amount, Map<String, Object> preferencesMap) {
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
