package com.sas.food_order_application.Adapter;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sas.food_order_application.R;
import com.sas.food_order_application.admin.AdminOrders;
import com.sas.food_order_application.user.UserLogin;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<DocumentSnapshot> tableDataList;
    FirebaseFirestore db;
    private List<CountDownTimer> countdownTimers;
    private boolean[] isTimerRunning;
    Context context;
    OkHttpClient client = new OkHttpClient();
    public OrderAdapter(List<DocumentSnapshot> tableDataList, Context context) {
        this.context = context;
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

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.TIRAMISU){
            if (checkSelfPermission(holder.itemView.getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) holder.itemView.getContext(), new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }
        DocumentSnapshot documentSnapshot = tableDataList.get(position);
        Context context = holder.itemView.getContext();

        String ID = String.valueOf(documentSnapshot.get("Id"));
        String tableNumber = String.valueOf(documentSnapshot.get("tablenumber"));
        String amount = String.valueOf( documentSnapshot.get("Total Amount"));

        Map<String, Object> preferencesMap = (Map<String, Object>) documentSnapshot.get("preferences");
        holder.bindData(ID, tableNumber, amount, preferencesMap);
        Button acceptButton = holder.itemView.findViewById(R.id.accept);
        Button reject=holder.itemView.findViewById(R.id.reject);
        Button orderready=holder.itemView.findViewById(R.id.order);
        orderready.setEnabled(false);

        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) { // 30 seconds
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
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
                    window.setBackgroundDrawableResource(R.drawable.rounddialog);
                    WindowManager.LayoutParams layoutParams = window.getAttributes();
                    layoutParams.width = dpToPx(holder.itemView.getContext(),400);
                    layoutParams.height = dpToPx(holder.itemView.getContext(),430);
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
                Log.d("id","is "+documentId);
                FirebaseFirestore.getInstance().collection("Order").document(documentId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                send();
            }

            private void markOrderAsReady(DocumentSnapshot documentSnapshot) {
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {

                                          Dialog dialog = new Dialog(holder.itemView.getContext());
                                          dialog.setContentView(R.layout.reject_splash);
                                          TextView maintxt = dialog.findViewById(R.id.txxxt);
                                          maintxt.setText("ORDER REJECTED");
                                          LottieAnimationView animationView = dialog.findViewById(R.id.lottiee);
                                          animationView.setAnimation(R.raw.reject);

                                          Window window = dialog.getWindow();
                                          if (window != null) {
                                              window.setBackgroundDrawableResource(R.drawable.rounddialog);
                                              WindowManager.LayoutParams layoutParams = window.getAttributes();
                                              layoutParams.width = dpToPx(holder.itemView.getContext(), 400);
                                              layoutParams.height = dpToPx(holder.itemView.getContext(), 430);

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
                tableDataList.remove(position);
                notifyItemRemoved(position);

                String documentId = documentSnapshot.getId();
                FirebaseFirestore.getInstance().collection("Order").document(documentId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            }
            private int dpToPx(Context context, int dp) {
                return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
            }
        });
    }

    private void sendNotificationToUser(String userDeviceToken) {
        try {
            // Construct your notification payload
            JSONObject notificationPayload = new JSONObject();
            notificationPayload.put("title", "Order Ready");
            notificationPayload.put("body", "Your order is ready!");

            // Create a request body with the notification payload
            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"), notificationPayload.toString());

            // Replace "YOUR_FCM_SERVER_KEY" with your actual FCM server key
            String fcmServerKey = "AAAA814xnYU:APA91bEZS0p-I4Uiifuqi5cAVKLALpmcLGDj2uTUddrRrUTR_MAQm9irUQLoQa8BLP93X9lR2pgwYrotTjychiKkRF653SK9vMbT6Z9-nSTVCd4RTZ_i2FlLzQUKRNcRBv47shFCqXHG";

            // Construct the HTTP POST request
            Request request = new Request.Builder()
                    .url("https://fcm.googleapis.com/fcm/send")
                    .addHeader("Authorization", "key=" + fcmServerKey)
                    .post(body)
                    .build();

            // Send the request and handle the response
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // Notification sent successfully
                System.out.println("Notification sent successfully");
            } else {
                System.out.println("Notification sending failed. Response code: " + response.code());
            }
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
        } catch (Exception e) {
            // Handle any other exception
            e.printStackTrace();
        }
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

        }
    }

    public void send(){
        String chanelId="CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,chanelId);
        builder.setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle("notifcation")
                .setContentTitle("Order is ready!..")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(context, UserLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel=notificationManager.getNotificationChannel(chanelId);
        if (notificationChannel==null){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            notificationChannel = new NotificationChannel(chanelId,"nicee",importance);
            notificationChannel.setLightColor(R.color.purple_500);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0,builder.build());
    }
}
