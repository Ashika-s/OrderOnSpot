package com.sas.food_order_application.ui.Settings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sas.food_order_application.R;
import com.sas.food_order_application.User_login;
import com.sas.food_order_application.Welcome;

public class SettingsFragment extends Fragment {
FirebaseAuth auth;
FirebaseUser user;
    ListView listView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String[] SettingItems = new String[]{"Edit Profile","Delete Account","LogOut"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private FragmentManager getSupportFragmentManager() {
        return null;
    }

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        listView=root.findViewById(R.id.list);
        String emaill =  User_login.emailid;
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,SettingItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==2) {
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    showDialog();
                } else if (position==0) {
                    Intent intent=new Intent(getActivity().getApplicationContext(),MyprofileActivity.class);
                    startActivity(intent);
                } else
                {
//                    db.collection("Customer").document(emaill)
//                            .delete()
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Log.d("delete account", "DocumentSnapshot successfully deleted!");
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w("delete account", "Error deleting document", e);
//                                }
//                            });
//
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//                    if (user != null) {
//                        user.delete()
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            // User account deleted successfully.
//                                            Log.d("delete", "User account deleted.");
//                                        } else {
//                                            // An error occurred.
//                                            Log.e("delete", "Error deleting user account: " + task.getException().getMessage());
//                                        }
//                                    }
//                                });
//                    } else {
//                        Log.d(TAG, "No user is currently signed in.");
//                    }
                    Toast.makeText(getActivity().getApplicationContext(),"non",Toast.LENGTH_SHORT).show();
                }
            }
            private  void profile(){
            }
            private void showDialog() {
                Context fragmentContext = requireContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(fragmentContext);
                builder.setTitle("Are you sure you want to logout?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(getActivity().getApplicationContext(), Welcome.class);
                        startActivity(intent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return root;
    }
}