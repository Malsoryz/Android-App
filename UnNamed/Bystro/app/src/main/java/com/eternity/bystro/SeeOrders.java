package com.eternity.bystro;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class SeeOrders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent getintent = getIntent();
        //product list
        int getproductid = getintent.getIntExtra("productid",0);
        String getname = getintent.getStringExtra("name");
        String gettype = getintent.getStringExtra("type");
        String getprice = getintent.getStringExtra("price");
        int getimage = getintent.getIntExtra("image",0);
        String getdesc = getintent.getStringExtra("desc");
        int getstock = getintent.getIntExtra("stock",0);

        //Address
        int getaddressid = getintent.getIntExtra("addressid",0);
        String getusername = getintent.getStringExtra("username");
        String getuseraddress = getintent.getStringExtra("useraddress");

        //Orders
        int getorderid = getintent.getIntExtra("orderid",0);
        int gettotalprice = getintent.getIntExtra("totalprice",0);
        int getquantity = getintent.getIntExtra("quantity",0);
        int getdelivery = getintent.getIntExtra("delivery",0);
        String getpayment = getintent.getStringExtra("payment");
        String getstatus = getintent.getStringExtra("status");

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();
        });

        ImageView photoframe = findViewById(R.id.photoframe);
        TextView productname = findViewById(R.id.productname);
        TextView type = findViewById(R.id.type);
        TextView quantity = findViewById(R.id.quantity);
        TextView price = findViewById(R.id.price);

        TextView orderstatus = findViewById(R.id.orderstatus);
        TextView recipient = findViewById(R.id.addressusername);

        TextView useraddress = findViewById(R.id.addresses);
        TextView subtotalprice = findViewById(R.id.subtotalprice);
        TextView totalitems = findViewById(R.id.totalitems);
        TextView subtotaldelivery = findViewById(R.id.subtotaldelivery);
        TextView paymentvia = findViewById(R.id.paymentvia);
        TextView totalprice = findViewById(R.id.totalprice);

        photoframe.setImageResource(getimage);
        productname.setText(getname);
        type.setText(gettype);
        quantity.setText(String.valueOf(getquantity));
        price.setText(MainActivity.formatIntToRP(Integer.parseInt(getprice)));

        recipient.setText(getusername);
        useraddress.setText(getuseraddress);

        orderstatus.setText(getstatus);
        int subtotal = Integer.parseInt(getprice) * getquantity;
        subtotalprice.setText(MainActivity.formatIntToRP(subtotal));
        totalitems.setText(String.valueOf(getquantity));
        subtotaldelivery.setText(MainActivity.formatIntToRP(getdelivery));
        paymentvia.setText(getpayment);
        totalprice.setText(MainActivity.formatIntToRP(gettotalprice));

        MaterialButton cancelorders = findViewById(R.id.cancelorders);
        cancelorders.setOnClickListener(cancel -> {
            AlertDialogDelete(getorderid,getproductid,getstock,getquantity);
            Toast.makeText(this, "Order Cancelled", Toast.LENGTH_SHORT).show();
        });
    }
    private MaterialAlertDialogBuilder AlertDialogDelete(int orderid, int productid, int stock, int quantity) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Do you want to delete it?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            BystroDatabase bystrodb = new BystroDatabase(this);
            bystrodb.cancelorders(orderid,productid,stock,quantity);
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.alert_dialog_bg);
        dialog.setOnShowListener(dialogInterface -> {
            TypedValue positivecolor = new TypedValue();
            getTheme().resolveAttribute(R.attr.Accent1, positivecolor, true);
            TypedValue negativecolor = new TypedValue();
            getTheme().resolveAttribute(R.attr.Accent1, negativecolor, true);

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(positivecolor.resourceId));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(negativecolor.resourceId));
        });
        dialog.show();
        return builder;
    }
}