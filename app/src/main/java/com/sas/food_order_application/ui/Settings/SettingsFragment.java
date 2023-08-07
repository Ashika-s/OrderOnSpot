package com.sas.food_order_application.ui.Settings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sas.food_order_application.R;
import com.sas.food_order_application.User_login;
import com.sas.food_order_application.Welcome;


public class SettingsFragment extends Fragment {

FirebaseAuth auth;
FirebaseUser user;
    ListView listView;
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


        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,SettingItems);
        listView.setAdapter(adapter);




//
//        root.findViewById(R.id.list).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(
//                        v
//                ).navigate(R.id.);
//            }
//        });
//

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==2) {
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    showDialog();
                } else if (position==0) {
//                    FragmentManager fragmentManager =requireActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.frameLayoutSetting,new ProfileFragment());
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                    String selectedItem = (String) parent.getItemAtPosition(position);
//
                    Intent intent=new Intent(getActivity().getApplicationContext(),MyprofileActivity.class);
                    startActivity(intent);
                } else
                {
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
                      //  user=auth.getCurrentUser();
                        FirebaseAuth.getInstance().signOut();
                     //   user=null;

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