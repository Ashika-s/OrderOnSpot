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

     class ViewHolder extends RecyclerView.ViewHolder{

        TextView categoryy;
        Item_Adapter item_adapter;
        RecyclerView itemViewRec;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryy=itemView.findViewById(R.id.textCategory);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Categoryclass> categoryclassList;
                    categoryclassList=itemSetListener(categoryy.getText().toString());
                    Intent intent = new Intent(context,Item_activity.class);

                 //   intent.putParcelableArrayListExtra("listitem", (ArrayList<? extends Parcelable>) categoryclassList);
                    context.startActivity(intent);
                }
            });

        }

        private ArrayList<Categoryclass> itemSetListener(String clickedCategory) {
            List<Categoryclass> listOfItems = new ArrayList<>();
            FirebaseFirestore db=FirebaseFirestore.getInstance();
            String emaill =  Admin_login.adminemailid;
            Log.d("email", "is " + emaill);

            // String email=Admin_login.adminemailid;
            DocumentReference docRef = db.collection("Admin").document(emaill);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                            AdminRegisterClass adminRegisterClass = document.toObject(AdminRegisterClass.class);
                            String rest = adminRegisterClass.getRestorantName();
                            Log.d("main", "is " + rest);

                            db.collection("Restaurant").document(rest).collection("Veg")
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                            if(error!=null)
                                            {
                                                Log.e("firestore error",error.getMessage());
                                                return;
                                            }
                                            for(DocumentChange dc : value.getDocumentChanges()){
                                                if(dc.getType()==DocumentChange.Type.ADDED){
                                                    Categoryclass category= dc.getDocument().toObject(Categoryclass.class);
                                                    if(clickedCategory.equals(category.getCategory())) {
                                                        listOfItems.add(dc.getDocument().toObject(Categoryclass.class));
                                                    }
                                                    Log.d("list"," is"+category.getCategory());
                                                }

                                                item_adapter.notifyDataSetChanged();
                                            }

                                        }
                                    });

                            db.collection("Restaurant").document(rest).collection("Non-Veg")
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                            if(error!=null)
                                            {
                                                Log.e("firestore error",error.getMessage());
                                                return;
                                            }
                                            for(DocumentChange dc : value.getDocumentChanges()){
                                                if(dc.getType()==DocumentChange.Type.ADDED){
                                                    Categoryclass category= dc.getDocument().toObject(Categoryclass.class);
                                                    if(category.equals(category.getCategory())) {
                                                        listOfItems.add(dc.getDocument().toObject(Categoryclass.class));
                                                    }
                                                    Log.d("list"," is"+category.getCategory());
                                                }

                                                item_adapter.notifyDataSetChanged();
                                            }
                                           // Log.d("templist",""+tempCategoryList.toString());

                                        }
                                    });
                        }
                    }
                }
            });

            return (ArrayList<Categoryclass>) listOfItems;
        }
    }
}