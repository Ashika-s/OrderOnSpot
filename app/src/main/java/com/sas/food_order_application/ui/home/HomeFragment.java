package com.sas.food_order_application.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.sas.food_order_application.Adapter.HomeCategoryUserAdapter;
import com.sas.food_order_application.Adapter.HomeItemUserAdapter;
import com.sas.food_order_application.admin.AdminRegisterClass;
import com.sas.food_order_application.databinding.FragmentHomeBinding;

import com.sas.food_order_application.Model.HomeCategoryUserModel;
import com.sas.food_order_application.Model.HomeItemUserModel;
import com.sas.food_order_application.R;
import com.sas.food_order_application.admin.Categoryclass;
import com.sas.food_order_application.user.CheckoutDetails;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeCategoryUserAdapter.ItemClickListener {

    TextView selectRestaurant;
    public static String selectedRest;
    List<String> documentIds = new ArrayList<>();
    Dialog dialog;
    Button btnNext;
    private FragmentHomeBinding binding;

    RecyclerView homeCategoryRec,homeItemRec;
    FirebaseFirestore firestore ;

    //Category list and adapter
    List<HomeCategoryUserModel> homeCategoryModelsList;
    HomeCategoryUserAdapter homeCategoryUserAdapter;

    //Dish List and adapter
    List<HomeItemUserModel> homeItemUserModelList;
    HomeItemUserAdapter homeItemUserAdapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        firestore=FirebaseFirestore.getInstance();
        selectRestaurant=root.findViewById(R.id.textSelectRestaurant);
        listOfRestaurant();
        homeCategoryRec = root.findViewById(R.id.categorylist);
        btnNext = root.findViewById(R.id.btntoOrderDetails);
        homeItemRec=root.findViewById(R.id.disheslist);
        homeCategoryModelsList = new ArrayList<>();
        homeItemUserModelList = new ArrayList<>();

        //working fine need to check after list of restaurant attached to listView
        selectRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new Dialog(root.getContext());
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(1200,1200);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view_restaurant);

                ArrayAdapter<String> adapter=new ArrayAdapter<>(root.getContext(), android.R.layout.simple_list_item_1,documentIds);
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectRestaurant.setText(adapter.getItem(position));
                        selectedRest=adapter.getItem(position);
                        dialog.dismiss();
                        homeCategoryModelsList.clear();
                        homeCategoryUserAdapter.notifyDataSetChanged();
                        fetchCategory();// logic for fetching category
//                        if(homeCategoryModelsList.size() >0) {
                           itemUpdate(homeCategoryModelsList.get(0).toString());
//                        }
                    }
                });
            }
        });


//        retreview  data which comes under specific category from firebase
        homeItemUserModelList.add(new HomeItemUserModel(R.drawable.burger,"Veg Salad","129"));
        homeItemUserModelList.add(new HomeItemUserModel(R.drawable.burger,"Veg Salad","129"));
        homeItemUserModelList.add(new HomeItemUserModel(R.drawable.burger,"Veg Salad","129"));
        homeItemUserModelList.add(new HomeItemUserModel(R.drawable.burger,"Veg Salad","129"));
        homeItemUserModelList.add(new HomeItemUserModel(R.drawable.burger,"Veg Salad","129"));
        homeItemUserModelList.add(new HomeItemUserModel(R.drawable.burger,"Veg Salad","129"));
        homeItemUserModelList.add(new HomeItemUserModel(R.drawable.burger,"Veg Salad","129"));
        homeItemUserModelList.add(new HomeItemUserModel(R.drawable.burger,"Veg Salad","129"));
        homeItemUserModelList.add(new HomeItemUserModel(R.drawable.burger,"Veg Salad","129"));



        homeCategoryUserAdapter = new HomeCategoryUserAdapter(getActivity(), homeCategoryModelsList);
        homeCategoryRec.setAdapter(homeCategoryUserAdapter);

        homeItemUserAdapter = new HomeItemUserAdapter(getActivity(),homeItemUserModelList);
        homeItemRec.setAdapter(homeItemUserAdapter);

        homeCategoryRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        homeCategoryRec.setHasFixedSize(true);
        homeCategoryRec.setNestedScrollingEnabled(false);

        homeItemRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        homeItemRec.setHasFixedSize(true);
        homeItemRec.setNestedScrollingEnabled(false);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext(), CheckoutDetails.class));
            }
        });

        return root;
    }

    //getting the document but method is correct
    private void listOfRestaurant() {
        firestore.collection("Admin").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult()!=null) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    AdminRegisterClass adminRegisterClass=document.toObject(AdminRegisterClass.class);
                                    String documentId = adminRegisterClass.getRestorantName();
                                    Log.d("document", documentId+"");
                                    documentIds.add(documentId);
                                }
                            }
                            Log.d("listOfrest",documentIds.toString()+"");
                        } else {
                            Log.e("firestore error", "not able to fetch the document ids");
                            Exception e =task.getException();
                        }
                    }
                });
    }

    //working good
    private void fetchCategory() {
        String selectedRestaurant=selectRestaurant.getText().toString();
        List<String> tempList = new ArrayList<>();
        DocumentReference docRef =firestore.collection("Restaurant").document(selectedRestaurant);

        docRef.collection("Veg").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("firestore error",error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        Categoryclass categoryclass = dc.getDocument().toObject(Categoryclass.class);
                        if(!tempList.contains(categoryclass.getCategory())){
                            tempList.add(categoryclass.getCategory());
                            HomeCategoryUserModel homeCategoryUserModel=new HomeCategoryUserModel(categoryclass.getCategory());
                            homeCategoryModelsList.add(homeCategoryUserModel);
                        }
                    }
                    homeCategoryUserAdapter.notifyDataSetChanged();
                }
            }
        });
        docRef.collection("Non-Veg").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("firestore error",error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        Categoryclass categoryclass = dc.getDocument().toObject(Categoryclass.class);
                        if(!tempList.contains(categoryclass.getCategory())){
                            tempList.add(categoryclass.getCategory());
                            HomeCategoryUserModel homeCategoryUserModel=new HomeCategoryUserModel(categoryclass.getCategory());
                            homeCategoryModelsList.add(homeCategoryUserModel);
                        }
                    }
                    homeCategoryUserAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClickListener(String item) {
        itemUpdate(item);
    }

    public void itemUpdate(String item){
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        String clickedCategory= item;
        Log.d(selectedRest ," "+selectedRest);
        homeItemUserModelList.clear();
        firestore.collection("Restaurant").document(selectedRest).collection("Veg")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Categoryclass category = dc.getDocument().toObject(Categoryclass.class);
                                if (clickedCategory.equals(category.getCategory())) {
                                    HomeItemUserModel homeItemUserModel = new HomeItemUserModel(R.drawable.burger, category.getItem(), category.getAmount());
                                    homeItemUserModelList.add(homeItemUserModel);
                                }

                            }
                            homeItemUserAdapter.notifyDataSetChanged();
                        }
                    }
                });

//        homeItemUserAdapter.notifyDataSetChanged();
    }

}
