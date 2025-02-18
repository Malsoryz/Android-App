package com.eternity.bystro;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ViewCheckout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent getintent = getIntent();
        int productid = getintent.getIntExtra("productid",0);
        int intentimage = getintent.getIntExtra("image",0);
        String intentproductname = getintent.getStringExtra("productname");
        String intenttype = getintent.getStringExtra("type");
        int intentquantity = getintent.getIntExtra("quantity",0);
        int intentprice = getintent.getIntExtra("price",0);
        int stock = getintent.getIntExtra("stock",0);

        //items
        ImageView photoframe = findViewById(R.id.photoframe);
        TextView productname = findViewById(R.id.productname);
        TextView type = findViewById(R.id.type);
        TextView quantity = findViewById(R.id.quantity);
        TextView perprice = findViewById(R.id.price);

        //address
        BystroDatabase bystrodb = new BystroDatabase(this);
        Cursor cursor = bystrodb.getTableValues("address");

        TextView addressusername = findViewById(R.id.addressusername);
        TextView addresses = findViewById(R.id.addresses);
        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            String useraddress = cursor.getString(cursor.getColumnIndexOrThrow("useraddress"));

            addressusername.setText(username);
            addresses.setText(useraddress);
        } else {
            addressusername.setText("None");
            addresses.setText("Select Address");
        }

        LinearLayout address = findViewById(R.id.setaddress);
        address.setOnClickListener(gotoaddress -> {
            Intent intent = new Intent(this,ViewSetAddress.class);
            startActivity(intent);
        });

        //payment via
        LinearLayout paymentvia = findViewById(R.id.paymentvia);
        TextView selectedpayment = findViewById(R.id.selectedpayment);
        paymentvia.setOnClickListener(paymentvia1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View paymentlayout = getLayoutInflater().inflate(R.layout.payment_alert_dialog,null);
            builder.setView(paymentlayout);
            AlertDialog dialog = builder.create();
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            LinearLayout codbtn = paymentlayout.findViewById(R.id.codpayment);
            codbtn.setOnClickListener(codpayment -> {
                selectedpayment.setText("COD");
                dialog.dismiss();
            });
            LinearLayout epay = paymentlayout.findViewById(R.id.electronicpayment);
            epay.setOnClickListener(epayment -> {
                selectedpayment.setText("E-Pay");
                dialog.dismiss();
            });
            dialog.show();
        });

        //calculate payment
        TextView subtotalprice = findViewById(R.id.subtotalprice);
        TextView totalitems = findViewById(R.id.totalitems);
        TextView subtotaldelivery = findViewById(R.id.subtotaldelivery);
        TextView totalpayment = findViewById(R.id.totalpayment);

        photoframe.setImageResource(intentimage);
        productname.setText(intentproductname);
        type.setText(intenttype);
        quantity.setText("Items "+String.valueOf(intentquantity));
        perprice.setText(MainActivity.formatIntToRP(intentprice));

        int delivery = getintent.getIntExtra("delivery",0);
        int subtotal = intentprice * intentquantity;
        int preprice = subtotal + delivery;
        subtotalprice.setText(MainActivity.formatIntToRP(subtotal));
        totalitems.setText(String.valueOf(intentquantity));
        subtotaldelivery.setText(MainActivity.formatIntToRP(delivery));
        totalpayment.setText(MainActivity.formatIntToRP(preprice));

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();
        });
        ImageButton refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(view -> {
            Toast.makeText(this,"Page refreshed",Toast.LENGTH_SHORT).show();
            recreate();
        });
        MaterialButton makeorder = findViewById(R.id.makeorder);
        makeorder.setOnClickListener(makeorder1 -> {
            if (cursor != null && cursor.moveToFirst()) {
                int getaddressid = cursor.getInt(cursor.getColumnIndexOrThrow("addressid"));
                String getpayment = selectedpayment.getText().toString();
                try {
                    bystrodb.makeOrder(productid,getaddressid,intentquantity,getpayment,preprice,delivery,stock);
                    Toast.makeText(this, "Product ordered!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            cursor.close();
        });
    }
}