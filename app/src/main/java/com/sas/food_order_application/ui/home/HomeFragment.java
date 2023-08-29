package com.sas.food_order_application.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
//import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sas.food_order_application.Adapter.HomeCategoryUserAdapter;
import com.sas.food_order_application.Adapter.HomeItemUserAdapter;
import com.sas.food_order_application.Model.HomeCategoryUserModel;
import com.sas.food_order_application.Model.HomeItemUserModel;
import com.sas.food_order_application.R;
import com.sas.food_order_application.admin.AdminRegisterClass;
import com.sas.food_order_application.admin.Categoryclass;
import com.sas.food_order_application.databinding.FragmentHomeBinding;
import com.sas.food_order_application.user.Checkout;
import com.sas.food_order_application.user.UserLogin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment  {

    TextView selectRestaurant;
    public static TextView txtItemsAdded;
    public static String selectedRest;
    SearchView searchView;
    List<String> documentIds = new ArrayList<>();
    Dialog dialog;
    static Button btnNext;
    private FragmentHomeBinding binding;

    RecyclerView homeCategoryRec,homeItemRec;
    static FirebaseFirestore firestore ;

    //Category list and adapter
    List<HomeCategoryUserModel> homeCategoryModelsList;
    public static List<Categoryclass> categoryClassList;
    HomeCategoryUserAdapter homeCategoryUserAdapter;

    //Dish List and adapter
    public static List<HomeItemUserModel> homeItemUserModelList;
    public static HomeItemUserAdapter homeItemUserAdapter;
    private Boolean isFirstLaunch;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        firestore=FirebaseFirestore.getInstance();
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentuser != null){
            SharedPreferences preferences = requireContext().getSharedPreferences("localEmailUser",MODE_PRIVATE);
            UserLogin.emailid=preferences.getString("KEY_EMAIL_USER", "");
        }
        selectRestaurant=root.findViewById(R.id.textSelectRestaurant);
        homeCategoryRec = root.findViewById(R.id.categorylist);
        txtItemsAdded=root.findViewById(R.id.itemsaddedcount);
        btnNext = root.findViewById(R.id.btntoOrderDetails);
        homeItemRec=root.findViewById(R.id.disheslist);
        searchView=root.findViewById(R.id.searchDishes);
        searchView.clearFocus();
        homeCategoryModelsList = new ArrayList<>();
        homeItemUserModelList = new ArrayList<>();
        categoryClassList = new ArrayList<>();

        listOfRestaurant();
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
                        HomeItemUserAdapter.dishList.clear();
                        categoryClassList.clear();
                        homeItemUserModelList.clear();
                        homeItemUserAdapter.notifyDataSetChanged();
                        homeCategoryModelsList.clear();
                        homeCategoryUserAdapter.notifyDataSetChanged();
                        fetchCategory();// logic for fetching category
                    }
                });
            }
        });
//        if (isFirstLaunch) {
//            selectRestaurant.performClick();
//            isFirstLaunch=false;
//        }
        //searchview
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        homeCategoryUserAdapter = new HomeCategoryUserAdapter(getActivity(), homeCategoryModelsList,categoryClassList);

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
                int size=HomeItemUserAdapter.dishList.size();
                if(size>0) {
                    Intent checkoutIntent = new Intent(root.getContext(),Checkout.class);
                    checkoutIntent.putExtra("Restaurant Name", selectedRest);
                    Log.d("restaurant",selectedRest);
                    startActivity(checkoutIntent);
                }else{
                    Toast.makeText(getContext(),"Add item(s) to checkout",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    //Btutton enable diable
    public static void setVisibility(int i){
        if(i>0){
            btnNext.setEnabled(true);
        }else {
            btnNext.setEnabled(false);
        }
    }

    //search dishes filter
    private void filterList(String txt){
        List<HomeItemUserModel> filterItemList = new ArrayList<>();
        for (HomeItemUserModel homeItemUserModel: homeItemUserModelList){
            if(homeItemUserModel.getDishName().toLowerCase(Locale.ROOT).contains(txt.toLowerCase(Locale.ROOT))){
                filterItemList.add(homeItemUserModel);
            }

            if (filterItemList.isEmpty()){
                Toast.makeText(getContext(),"No Item found",Toast.LENGTH_SHORT).show();
            }else {
                homeItemUserAdapter.setFilteredList(filterItemList);
            }
        }
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
                        categoryClassList.add(categoryclass);
                        //extra
                        HomeItemUserModel homeItemUserModel=new HomeItemUserModel(categoryclass.getItem(),categoryclass.getAmount(),String.valueOf(0), categoryclass.getType());
                        homeItemUserModelList.add(homeItemUserModel);

                        if(!tempList.contains(categoryclass.getCategory())){
                            tempList.add(categoryclass.getCategory());
                            HomeCategoryUserModel homeCategoryUserModel=new HomeCategoryUserModel(categoryclass.getCategory());
                            homeCategoryModelsList.add(homeCategoryUserModel);
                        }
                    }
                    homeItemUserAdapter.notifyDataSetChanged();
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
                        categoryClassList.add(categoryclass);
                        HomeItemUserModel homeItemUserModel=new HomeItemUserModel(categoryclass.getItem(),categoryclass.getAmount(),String.valueOf(0),categoryclass.getType());
                        homeItemUserModelList.add(homeItemUserModel);
                        if(!tempList.contains(categoryclass.getCategory())){
                            tempList.add(categoryclass.getCategory());
                            HomeCategoryUserModel homeCategoryUserModel=new HomeCategoryUserModel(categoryclass.getCategory());
                            homeCategoryModelsList.add(homeCategoryUserModel);
                        }
                    }
                    homeItemUserAdapter.notifyDataSetChanged();
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

    public static void getUpdateAdapter(List<HomeItemUserModel> homeItemUserModel){
//        homeItemUserModelList.clear();
//        homeItemUserModelList.addAll(homeItemUserModel);
//        homeItemUserAdapter.notifyDataSetChanged();
        homeItemUserAdapter.setFilteredList(homeItemUserModel);
    }


}
