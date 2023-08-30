package com.sas.food_order_application.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.sas.food_order_application.R;
import com.sas.food_order_application.user.Feedback;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

    public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {
        private List<DocumentSnapshot> feedbacklist;
        Context context;

        public FeedbackAdapter(List<DocumentSnapshot> feedbacklist, Context context) {
            this.context = context;
            this.feedbacklist = feedbacklist;
        }

        @NonNull
        @Override
        public FeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedbackview, parent, false);
            return new FeedbackAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FeedbackAdapter.ViewHolder holder, int position) {
            DocumentSnapshot documentSnapshot = feedbacklist.get(position);
            Context context = holder.itemView.getContext();
            int ID = Integer.parseInt(String.valueOf(documentSnapshot.get("orderid")));
            String message=String.valueOf(documentSnapshot.get("message"));
            float rate= Float.parseFloat(String.valueOf(documentSnapshot.get("rating")));
            holder.bindData(ID,message,rate);
        }

        @Override
        public int getItemCount() {
            return feedbacklist.size(); // Return the actual size of the data list
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView Id;
            TextView message;
            RatingBar ratingBar;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                Id = itemView.findViewById(R.id.feedid);
                message = itemView.findViewById(R.id.feedmsgg);
                ratingBar = itemView.findViewById(R.id.feedratingBar);
            }

            public void bindData(int id,String msg,float rating) {
                Id.setText("ID : " + id);
                message.setText("Message : " + msg);
                ratingBar.setRating(rating);
            }
        }
    }
