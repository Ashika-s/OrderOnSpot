package com.sas.food_order_application.admin;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sas.food_order_application.Adapter.Item_Adapter;
import com.sas.food_order_application.R;
import java.util.ArrayList;
import java.util.List;

public class Item_activity extends AppCompatActivity {
    Item_Adapter item_adapter;
    RecyclerView itemViewRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        itemViewRec=findViewById(R.id.recyclerviewItem);
        Intent intent=getIntent();
        String categoryy=intent.getStringExtra("clicedString");
        List<Categoryclass> categoryclassList;
        categoryclassList=itemSetListener(categoryy);
        Log.d("clickedList",""+categoryclassList.toString());
        itemViewRec.setHasFixedSize(true);
        itemViewRec.setLayoutManager(new LinearLayoutManager(this));
        item_adapter=new Item_Adapter(Item_activity.this,categoryclassList);
        itemViewRec.setAdapter(item_adapter);
    }

    private ArrayList<Categoryclass> itemSetListener(String clickedCategory) {
        List<Categoryclass> listOfItems = new ArrayList<>();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        String emaill =  Admin_login.adminemailid;
        Log.d("email", "is " + emaill);
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
                                                if(clickedCategory.equals(category.getCategory())) {
                                                    listOfItems.add(dc.getDocument().toObject(Categoryclass.class));
                                                }
                                                Log.d("list"," is"+category.getCategory());
                                            }
                                            item_adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                    }
                }
            }
        });
        return (ArrayList<Categoryclass>) listOfItems;
    }
}