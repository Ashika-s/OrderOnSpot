package com.sas.food_order_application.admin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sas.food_order_application.R;

public class orders extends Fragment {
View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
                view=inflater.inflate(R.layout.fragment_orders, container, false);
        return view;
    }
}