package com.eternity.bystro;

import static java.security.AccessController.getContext;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class ViewSetAddress extends AppCompatActivity {

    private boolean editmode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_set_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BystroDatabase bystrodb = new BystroDatabase(this);
        Cursor cursor = bystrodb.getTableValues("address");

        //show registered address
        LinearLayout addressframe = findViewById(R.id.addressframe);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int addressid = cursor.getInt(cursor.getColumnIndexOrThrow("addressid"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String useraddress = cursor.getString(cursor.getColumnIndexOrThrow("useraddress"));

                View addressitem = getLayoutInflater().inflate(R.layout.addressitem,addressframe,false);
                LinearLayout selectaddress = addressitem.findViewById(R.id.selectaddress);
                TextView addressusername = addressitem.findViewById(R.id.addressusername);
                TextView addresses = addressitem.findViewById(R.id.addresses);
                ImageButton more = addressitem.findViewById(R.id.more);

                addressusername.setText(username);
                addresses.setText(useraddress);
                addressframe.addView(addressitem);

                String addressuser = addressusername.getText().toString();
                String addressed = addresses.getText().toString();

                selectaddress.setOnClickListener(selectaddress1 -> {
                    bystrodb.selectAddress(addressid);
                    Toast.makeText(this,"Please refresh the page to see the changes",Toast.LENGTH_SHORT).show();
                    finish();
                });

                more.setOnClickListener(view -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    View morelayout = getLayoutInflater().inflate(R.layout.address_more_alert_dialog,null);
                    builder.setView(morelayout);
                    AlertDialog dialog = builder.create();
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    MaterialButton updatebtn = morelayout.findViewById(R.id.update);
                    MaterialButton deletebtn = morelayout.findViewById(R.id.delete);

                    updatebtn.setOnClickListener(updateview -> {
                        AlertDialogUpdate(addressuser,addressed,addressid);
                    });
                    deletebtn.setOnClickListener(deleteview -> {
                        AlertDialogDelete(addressid);
                        dialog.dismiss();
                    });
                    dialog.show();;
                });
            } while (cursor.moveToNext());
            cursor.close();
        }

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();
        });

        //dialog add new address
        MaterialButton addnewaddress = findViewById(R.id.addnewaddress);
        addnewaddress.setOnClickListener(view -> {
            AddNewAddressAlertDialog();
        });
    }
    private AlertDialog AlertDialogUpdate(String addressusername, String addresses, int addressid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View updatelayout = getLayoutInflater().inflate(R.layout.add_new_address_dialog,null);
        builder.setView(updatelayout);
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = updatelayout.findViewById(R.id.addressdialogtitle);
        EditText sendernameinput = updatelayout.findViewById(R.id.sendernameinput);
        EditText addressinput = updatelayout.findViewById(R.id.addressinput);
        MaterialButton addaddressbtn = updatelayout.findViewById(R.id.addaddress);

        title.setText("Update Address");
        sendernameinput.setText(addressusername);
        addressinput.setText(addresses);
        addaddressbtn.setOnClickListener(updating -> {
            String getsendername = sendernameinput.getText().toString();
            String getaddress = addressinput.getText().toString();
            if (!getsendername.isEmpty() && !getaddress.isEmpty()) {
                BystroDatabase bystrodb = new BystroDatabase(this);
                bystrodb.updateAddress(getsendername,getaddress,addressid);
                Toast.makeText(this,"Address added!",Toast.LENGTH_SHORT).show();
                recreate();
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }
    private MaterialAlertDialogBuilder AlertDialogDelete(int addressid) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Do you want to delete it?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            BystroDatabase bystrodb = new BystroDatabase(this);
            bystrodb.deleteAddress(addressid);
            recreate();
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
    private AlertDialog AddNewAddressAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View addaddress = getLayoutInflater().inflate(R.layout.add_new_address_dialog,null);
        builder.setView(addaddress);
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = addaddress.findViewById(R.id.addressdialogtitle);
        EditText sendernameinput = addaddress.findViewById(R.id.sendernameinput);
        EditText addressinput = addaddress.findViewById(R.id.addressinput);
        MaterialButton addaddressbtn = addaddress.findViewById(R.id.addaddress);

        title.setText("Add New Address");
        addaddressbtn.setOnClickListener(view2 -> {
            String getsendername = sendernameinput.getText().toString();
            String getaddress = addressinput.getText().toString();
            if (!getsendername.isEmpty() && !getaddress.isEmpty()) {
                BystroDatabase bystrodb = new BystroDatabase(this);
                bystrodb.addNewAddress(getsendername,getaddress);
                Toast.makeText(this,"Address added!",Toast.LENGTH_SHORT).show();
                recreate();
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }
}