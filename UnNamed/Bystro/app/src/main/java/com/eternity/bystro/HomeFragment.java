package com.eternity.bystro;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        GridLayout listorder = rootview.findViewById(R.id.listorder);

        BystroDatabase bystrodb = new BystroDatabase(getContext());
        Cursor cursor = bystrodb.getTableValues("product_list");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("productid"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                    String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                    int image = cursor.getInt(cursor.getColumnIndexOrThrow("image"));
                    String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    int stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock"));

                    View listitem = inflater.inflate(R.layout.homeitem, listorder, false);
                    MaterialCardView cardview = listitem.findViewById(R.id.cardview);
                    ImageView photoframe = listitem.findViewById(R.id.photoframe);
                    TextView productname = listitem.findViewById(R.id.homeproductname);
                    TextView productprice = listitem.findViewById(R.id.homeproductprice);

                    cardview.setOnClickListener(view -> {
                        Intent intent = new Intent(requireContext(),ViewProduct.class);
                        intent.putExtra("productid",id);
                        intent.putExtra("name",name);
                        intent.putExtra("type",type);
                        intent.putExtra("price",price);
                        intent.putExtra("image",image);
                        intent.putExtra("desc",desc);
                        intent.putExtra("stock",stock);

                        startActivity(intent);
                    });

                    photoframe.setImageResource(image);
                    photoframe.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                        int width = photoframe.getWidth();
                        photoframe.getLayoutParams().height = width;
                        photoframe.requestLayout();
                    });

                    productname.setText(name);
                    int preprice = Integer.parseInt(price);
                    productprice.setText(MainActivity.formatIntToRP(preprice));

                    listorder.addView(listitem);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return rootview;
    }


}
