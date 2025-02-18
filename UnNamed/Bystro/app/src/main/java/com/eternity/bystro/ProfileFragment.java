package com.eternity.bystro;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        BystroDatabase bystrodb = new BystroDatabase(getContext());
        MainActivity mainActivity = (MainActivity) getActivity();

        TextView editaddress = rootview.findViewById(R.id.editaddress);
        TextView resetdatabase = rootview.findViewById(R.id.resetdatabase);
        TextView resetproductlist = rootview.findViewById(R.id.resetproductlist);
        TextView resetaddress = rootview.findViewById(R.id.resetaddress);
        TextView resetorders = rootview.findViewById(R.id.resetorders);
        TextView refresh = rootview.findViewById(R.id.refresh);

        editaddress.setOnClickListener(edit_address -> {
            Intent intent = new Intent(requireContext(), ViewSetAddress.class);
            startActivity(intent);
        });
        resetdatabase.setOnClickListener(reset_database -> {
            bystrodb.recreateTable("product_list",BystroDatabase.CREATE_PRODUCT_LIST_TABLE);
            bystrodb.recreateTable("address",BystroDatabase.CREATE_ADDRESS_TABLE);
            bystrodb.recreateTable("orders",BystroDatabase.CREATE_ORDER_TABLE);
            assert mainActivity != null;
            bystrodb.addProduct(mainActivity.getProductlistwithid());
            Toast.makeText(mainActivity, "Successfully reset database", Toast.LENGTH_SHORT).show();
        });
        resetproductlist.setOnClickListener(reset_productlist -> {
            bystrodb.recreateTable("product_list",BystroDatabase.CREATE_PRODUCT_LIST_TABLE);
            assert mainActivity != null;
            bystrodb.addProduct(mainActivity.getProductlistwithid());
            Toast.makeText(mainActivity, "Successfully reset product list", Toast.LENGTH_SHORT).show();
        });
        resetaddress.setOnClickListener(reset_address -> {
            bystrodb.recreateTable("address",BystroDatabase.CREATE_ADDRESS_TABLE);
            Toast.makeText(mainActivity, "Successfully reset Address", Toast.LENGTH_SHORT).show();
        });
        resetorders.setOnClickListener(reset_orders -> {
            bystrodb.recreateTable("orders",BystroDatabase.CREATE_ORDER_TABLE);
            Toast.makeText(mainActivity, "Successfully reset orders", Toast.LENGTH_SHORT).show();
        });
        refresh.setOnClickListener(refresh1 -> {
            requireActivity().recreate();
        });

        return rootview;
    }
}