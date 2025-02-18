package com.eternity.datasiswa;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        siswaDatabase db = new siswaDatabase(this);

        ListView dataDisplay = findViewById(R.id.displaydata);
        Cursor cursor = db.selectSiswa();

        siswaAdapter adapter = new siswaAdapter(this, cursor, 0);
        dataDisplay.setAdapter(adapter);

        //dialog untuk menambahkan data
        Dialog addData = new Dialog(MainActivity.this);
        FloatingActionButton floatingbtn = findViewById(R.id.floatingbtn);
        floatingbtn.setOnClickListener(view -> {
            addData.setContentView(R.layout.inputform);
            addData.setCancelable(true);
            Objects.requireNonNull(addData.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            ImageButton closeDialog = addData.findViewById(R.id.closeDialog);
            EditText inputNama = addData.findViewById(R.id.inputnama);
            EditText inputNilaiHarian = addData.findViewById(R.id.nilaiHarian);
            EditText inputNilaiTugas = addData.findViewById(R.id.nilaiTugas);
            EditText inputNilaiAkhir = addData.findViewById(R.id.nilaiUlangan);
            Button submitButton = addData.findViewById(R.id.submitButton);

            //filter angka
            inputNilaiHarian.setFilters(new InputFilter[]{new InputFilterMinMax(0,100)});
            inputNilaiTugas.setFilters(new InputFilter[]{new InputFilterMinMax(0,100)});
            inputNilaiAkhir.setFilters(new InputFilter[]{new InputFilterMinMax(0,100)});

            closeDialog.setOnClickListener(cd -> {
                addData.cancel();
            });

            submitButton.setOnClickListener(d -> {
                String nama = inputNama.getText().toString();

                //cek nilai nilai nya jika kosong = 0
                int harianInt = inputNilaiHarian.getText().toString().isEmpty() ? 0 : Integer.parseInt(inputNilaiHarian.getText().toString());
                int tugasInt = inputNilaiTugas.getText().toString().isEmpty() ? 0 : Integer.parseInt(inputNilaiTugas.getText().toString());
                int akhirInt = inputNilaiAkhir.getText().toString().isEmpty() ? 0 : Integer.parseInt(inputNilaiAkhir.getText().toString());

                db.addSiswa(nama, harianInt, tugasInt, akhirInt);
                recreate();
            });

            addData.show();
        });

        //jika data kosong maka akan ditampilkan layout kosong dengan sedikit deskripsi
        if (db.isSiswaEmpty()) {
            LinearLayout main = findViewById(R.id.main);
            LayoutInflater inflater = LayoutInflater.from(this);
            View dataEmpty = inflater.inflate(R.layout.data_empty, main, false);
            main.removeAllViews();
            main.addView(dataEmpty);
        }

    }
}