package com.sas.food_order_application.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sas.food_order_application.Model.TimeModel;
import com.sas.food_order_application.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<DocumentSnapshot> tableDataList;
    FirebaseFirestore db;

    private List<CountDownTimer> countdownTimers; // List to store active timers
    private boolean[] isTimerRunning;
    public OrderAdapter(List<DocumentSnapshot> tableDataList) {
        this.tableDataList = tableDataList;
        countdownTimers = new ArrayList<>();
        isTimerRunning = new boolean[tableDataList.size()];
        Arrays.fill(isTimerRunning, false);
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_orderview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DocumentSnapshot documentSnapshot = tableDataList.get(position);

        String ID = String.valueOf(documentSnapshot.get("Id"));
        String tableNumber = String.valueOf(documentSnapshot.get("tablenumber"));
        String amount = String.valueOf( documentSnapshot.get("Total Amount"));

        Map<String, Object> preferencesMap = (Map<String, Object>) documentSnapshot.get("preferences");
        holder.bindData(ID, tableNumber, amount, preferencesMap);
        Button acceptButton = holder.itemView.findViewById(R.id.accept);
        Button reject=holder.itemView.findViewById(R.id.reject);
        Button orderready=holder.itemView.findViewById(R.id.order);
        orderready.setEnabled(false);

        //   if (!isTimerRunning[position]) {
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) { // 30 seconds
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                //    countdownTextView.setText("Time left: " + seconds + "s");
                acceptButton.setText("Accept (" + seconds + "s)");
            }

            @Override
            public void onFinish() {
                acceptButton.setEnabled(false);
                isTimerRunning[position] = true;
            }
        };
        countDownTimer.start();
        countdownTimers.add(countDownTimer);
        isTimerRunning[position] = false;
        // }


        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownTimers.get(position).cancel();

                Dialog dialog = new Dialog(holder.itemView.getContext());
                dialog.setContentView(R.layout.activity_orderready_splascreensh);
                TextView maintxt = dialog.findViewById(R.id.txxt);
                maintxt.setText("ORDER ACCEPTED");
                TextView subtxt = dialog.findViewById(R.id.subtxxt);
                subtxt.setText("Taking you to next Order...");

                LottieAnimationView animationView = dialog.findViewById(R.id.lottie);
                animationView.setAnimation(R.raw.done);



                Window window = dialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams layoutParams = window.getAttributes();
                    layoutParams.width = dpToPx(holder.itemView.getContext(),400); // Convert dp to pixels
                    layoutParams.height = dpToPx(holder.itemView.getContext(),500); // Convert dp to pixels
                    window.setAttributes(layoutParams);
                }

                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }, 3000);
                reject.setEnabled(false);
                orderready.setEnabled(true);
                acceptButton.setText("Accepted");
                acceptButton.setEnabled(false);// Set the button text to "Accepted"
            }

            private int dpToPx(Context context, int dp) {
                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
            }
        });


        orderready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tableDataList.remove(position);
                notifyItemRemoved(position);
                String documentId = documentSnapshot.getId();
                FirebaseFirestore.getInstance().collection("Order").document(documentId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Item deleted successfully from the database
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure to delete item from the database
                            }
                        });

            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableDataList.remove(position);
                notifyItemRemoved(position);

                String documentId = documentSnapshot.getId();
                FirebaseFirestore.getInstance().collection("Order").document(documentId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Item deleted successfully from the database
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure to delete item from the database
                            }
                        });
                // orderready.


            }
        });

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

        Button accept;
        Button reject;
        TextView time;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Id = itemView.findViewById(R.id.id);
            Tablenumber = itemView.findViewById(R.id.tableno);
            TotalAmount=itemView.findViewById(R.id.totalAmount);
            preferencesTextView = itemView.findViewById(R.id.preferencesTextView);


        }

        public void bindData(String id,String tableNumber, String amount, Map<String, Object> preferencesMap) {

            Id.setText("ID : "+id);
            Tablenumber.setText("TABLE NUMBER : "+tableNumber);
            TotalAmount.setText("AMOUNT : "+amount);

            StringBuilder preferencesText = new StringBuilder();
            for (Map.Entry<String, Object> entry : preferencesMap.entrySet()) {
                preferencesText.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
            }
            preferencesTextView.setText(preferencesText.toString());
            Button acceptButton = itemView.findViewById(R.id.accept);

        }
    }
}