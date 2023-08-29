package com.sas.food_order_application.ui.My_Orders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sas.food_order_application.Adapter.MyordersAdapter;
import com.sas.food_order_application.Adapter.OrderAdapter;
import com.sas.food_order_application.R;
import com.sas.food_order_application.admin.AdminLogin;
import com.sas.food_order_application.databinding.FragmentHomeBinding;
import com.sas.food_order_application.user.UserLogin;

import java.util.ArrayList;
import java.util.List;

public class MyordersFragment extends Fragment {

    List<DocumentSnapshot> tableList;
    RecyclerView recyclerView;
    MyordersAdapter tableDataAdapter;
    String email = UserLogin.emailid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_myorders, container, false);
        recyclerView = root.findViewById(R.id.recyclerviewmyorders);
        tableList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        getorders();
        return root;
    }

    private void getorders() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tableDataCollection = db.collection("My_Orders");

        tableDataCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String usermail = documentSnapshot.getString("email");
                    boolean check = usermail.equals(email);
                    Log.d("orders", "is " + usermail);
                    Log.d("orders", "is " + email);
                    Log.d("orders", "is " + check);
                    if (check) {
                        Log.d("order", "is " + usermail + " " + email);
                        tableList.add(documentSnapshot);
                    }
                }
                // Initialize and set up the adapter after adding data
                tableDataAdapter = new MyordersAdapter(tableList, getActivity().getApplicationContext());
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(tableDataAdapter);

                // Notify the adapter about the data change
                tableDataAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure
            }
        });
    }
}
