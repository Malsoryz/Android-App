package com.eternity.bystro;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    public OrdersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_orders, container, false);

        BystroDatabase bystrodb = new BystroDatabase(getContext());
        Cursor cursor = bystrodb.getAllTable();

        LinearLayout listorder = rootview.findViewById(R.id.listorder);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    View orderitem = inflater.inflate(R.layout.orderitem,listorder,false);
                    LinearLayout seeorders = orderitem.findViewById(R.id.seeorders);
                    ImageView photoframe = orderitem.findViewById(R.id.photoframe);
                    TextView productname = orderitem.findViewById(R.id.productname);
                    TextView type = orderitem.findViewById(R.id.type);
                    TextView price = orderitem.findViewById(R.id.price);
                    TextView quantity = orderitem.findViewById(R.id.quantity);
                    TextView status = orderitem.findViewById(R.id.status);

                    //from product_list
                    int getproductid = cursor.getInt(cursor.getColumnIndexOrThrow("productid"));
                    String getname = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String gettype = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                    String getprice = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                    int getimage = cursor.getInt(cursor.getColumnIndexOrThrow("image"));
                    String getdesc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    int getstock = cursor.getInt(cursor.getColumnIndexOrThrow("stock"));

                    //from address
                    int getaddressid = cursor.getInt(cursor.getColumnIndexOrThrow("addressid"));
                    String getusername = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                    String getuseraddress = cursor.getString(cursor.getColumnIndexOrThrow("useraddress"));

                    //from orders
                    int getorderid = cursor.getInt(cursor.getColumnIndexOrThrow("orderid"));
                    int gettotalprice = cursor.getInt(cursor.getColumnIndexOrThrow("totalprice"));
                    int getquantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                    int getdelivery = cursor.getInt(cursor.getColumnIndexOrThrow("delivery"));
                    String getpayment = cursor.getString(cursor.getColumnIndexOrThrow("payment"));
                    String getstatus = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                    photoframe.setImageResource(getimage);
                    productname.setText(getname);
                    type.setText(gettype);
                    price.setText(MainActivity.formatIntToRP(gettotalprice));
                    quantity.setText(String.valueOf(getquantity));
                    status.setText(getstatus);

                    seeorders.setOnClickListener(see_orders -> {
                        Intent intent = new Intent(requireContext(), SeeOrders.class);

                        //product list
                        intent.putExtra("productid",getproductid);
                        intent.putExtra("name",getname);
                        intent.putExtra("type",gettype);
                        intent.putExtra("price",getprice);
                        intent.putExtra("image",getimage);
                        intent.putExtra("desc",getdesc);
                        intent.putExtra("stock",getstock);

                        //address
                        intent.putExtra("addressid",getaddressid);
                        intent.putExtra("username",getusername);
                        intent.putExtra("useraddress",getuseraddress);

                        //orders
                        intent.putExtra("orderid",getorderid);
                        intent.putExtra("totalprice",gettotalprice);
                        intent.putExtra("quantity",getquantity);
                        intent.putExtra("delivery",getdelivery);
                        intent.putExtra("payment",getpayment);
                        intent.putExtra("status",getstatus);

                        startActivity(intent);
                    });

                    listorder.addView(orderitem);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rootview;
    }
}