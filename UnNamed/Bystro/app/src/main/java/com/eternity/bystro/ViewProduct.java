package com.eternity.bystro;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ViewProduct extends AppCompatActivity {

    private BystroDatabase bystrodb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bystrodb = new BystroDatabase(this);

        Intent getintent = getIntent();
        int productid = getintent.getIntExtra("productid",0);
        String productname = getintent.getStringExtra("name");
        String type = getintent.getStringExtra("type");
        int price = Integer.parseInt(Objects.requireNonNull(getintent.getStringExtra("price")));
        int image = getintent.getIntExtra("image",0);
        String desc = getintent.getStringExtra("desc");
        int stock = getintent.getIntExtra("stock",0);

        ImageView photoframe = findViewById(R.id.photoframe);
        TextView productview = findViewById(R.id.productname);
        TextView typeview = findViewById(R.id.type);
        TextView priceview = findViewById(R.id.price);
        TextView stockview = findViewById(R.id.stock);
        TextView descview = findViewById(R.id.desc);
        TextView amount = findViewById(R.id.itemamount);
        TextView pretotalprice = findViewById(R.id.pretotal);
        ImageButton less = findViewById(R.id.lessquantity);
        TextView howmuch = findViewById(R.id.howmuchadd);
        ImageButton add = findViewById(R.id.addquantity);
        MaterialButton checkout = findViewById(R.id.checkout);

        String getquantity = howmuch.getText().toString();
        int quanty = Integer.parseInt(getquantity);
        int randomizedelivery = 15 + (int)(Math.random() * (31 - 15 + 1));
        int delivery = randomizedelivery * 1000;

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();
        });

        less.setOnClickListener(view -> {
            String much = howmuch.getText().toString();
            float quantity = Float.parseFloat(much);
            if (quantity > 0) {
                quantity -= 1;
                howmuch.setText(String.valueOf((int) quantity));
                amount.setText("Amount : "+String.valueOf((int) quantity));
                int totalprice = (int) (price * quantity);
                pretotalprice.setText("Total : "+MainActivity.formatIntToRP(totalprice));
            }
        });
        add.setOnClickListener(view -> {
            String much = howmuch.getText().toString();
            float quantity = Float.parseFloat(much);
            if (quantity < stock) {
                quantity += 1;
                howmuch.setText(String.valueOf((int) quantity));
                amount.setText("Amount : "+String.valueOf((int) quantity));
                int totalprice = (int) (price * quantity);
                pretotalprice.setText("Total : "+MainActivity.formatIntToRP(totalprice));
            }
        });

        photoframe.setImageResource(image);
        photoframe.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int width = photoframe.getWidth();
            photoframe.getLayoutParams().height = width;
            photoframe.requestLayout();
        });
        productview.setText(productname);
        typeview.setText(type);
        priceview.setText(MainActivity.formatIntToRP(price));
        stockview.setText("Stock " + stock);
        descview.setText(desc);

        checkout.setOnClickListener(view -> {
            int quantity = Integer.parseInt(howmuch.getText().toString());
            if (quantity > 0) {
                Intent intent = new Intent(this,ViewCheckout.class);
                intent.putExtra("productid",productid);
                intent.putExtra("image",image);
                intent.putExtra("productname",productname);
                intent.putExtra("type",type);
                intent.putExtra("quantity",Integer.parseInt(howmuch.getText().toString()));
                intent.putExtra("price",price);
                intent.putExtra("stock",stock);
                intent.putExtra("delivery",delivery);
                startActivity(intent);
            } else {
                Toast.makeText(this,"Add items first!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}