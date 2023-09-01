package com.sas.food_order_application.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sas.food_order_application.Model.CheckoutModel;
import com.sas.food_order_application.Model.HomeItemUserModel;
import com.sas.food_order_application.R;
import com.sas.food_order_application.ui.home.HomeFragment;
import com.sas.food_order_application.user.Checkout;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CheckoutUserAdapter extends RecyclerView.Adapter<CheckoutUserAdapter.ViewHolder> {

    Context context;
    public static List<CheckoutModel> checkoutModelList;
    StorageReference storageReference;

    public CheckoutUserAdapter(Context context, List<CheckoutModel> list) {
        this.context = context;
        this.checkoutModelList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckoutModel checkoutModel=checkoutModelList.get(position);
        holder.txtDishName.setText(checkoutModelList.get(position).getDishName());
        holder.edtTxtItemCount.setText((checkoutModelList.get(position).getDishQuantity()));
        holder.txtDishAmount.setText(checkoutModelList.get(position).getDishAmount());
        try {
            storageReference = FirebaseStorage.getInstance().getReference("images/"+checkoutModel.getDishName()+".jpg");
            File localfile1 = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localfile1)
                    .addOnSuccessListener(taskSnapshot1 -> {
                        Bitmap bitmap1 = BitmapFactory.decodeFile(localfile1.getAbsolutePath());
                        holder.imageDish.setImageBitmap(bitmap1);
                    }).addOnFailureListener(e -> Toast.makeText(context, "Failed to retrieve", Toast.LENGTH_SHORT).show());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getItemCount() {
        return checkoutModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDishName,txtDishAmount;
        EditText edtTxtItemCount;
        Button btnDecrease,btnIncrease;
        ImageView imageDish,closeImg;

        int minteger=0;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDishAmount=itemView.findViewById(R.id.txtCheckoutDishAmount);
            txtDishName=itemView.findViewById(R.id.txtCheckoutDishName);
            imageDish=itemView.findViewById(R.id.imageCheckoutDish);
            edtTxtItemCount=itemView.findViewById(R.id.integer_number);
            closeImg=itemView.findViewById(R.id.closeImg);
            closeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
//                    for(HomeItemUserModel str : HomeItemUserAdapter.dishList) {
//                        if(str.getDishName().equals(checkoutModelList.get(position).getDishName())) {
                            HomeItemUserAdapter.dishList.remove(position);
                            HomeFragment.txtItemsAdded.setText(HomeItemUserAdapter.dishList.size() + " Items Added");
//                        }
//                    }
                    Log.d("CheckoutItem Remove","list size "+HomeItemUserAdapter.dishList.size()+" "+HomeFragment.txtItemsAdded.getText().toString());
                    checkoutModelList.remove(position);
                    Checkout.setTotalAmount();
                    notifyDataSetChanged();
                }
            });
            edtTxtItemCount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int position = getAdapterPosition();
                    CheckoutModel checkoutModel= checkoutModelList.get(position);
                    checkoutModel.setDishQuantity(s.toString());
                    checkoutModelList.set(position,checkoutModel);
                    Log.d("Quantity",checkoutModel.getDishName()+"is"+ checkoutModel.getDishQuantity());


                }

                @SuppressLint("SuspiciousIndentation")
                @Override
                public void afterTextChanged(Editable s) {
                    if (edtTxtItemCount.getText().toString() != "")
                    Checkout.setTotalAmount();
                }
            });
            edtTxtItemCount.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode==KeyEvent.KEYCODE_BACK || keyCode==KeyEvent.KEYCODE_ENTER){
                        edtTxtItemCount.clearFocus();
                        hideSoftKeyBoard();
                        return true;
                    }
                    return ViewHolder.super.itemView.onKeyDown(keyCode,event);
                }
            });

        }
        private void hideSoftKeyBoard() {
            InputMethodManager im=(InputMethodManager) itemView.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow( edtTxtItemCount.getWindowToken(),0);
        }


    }
}
