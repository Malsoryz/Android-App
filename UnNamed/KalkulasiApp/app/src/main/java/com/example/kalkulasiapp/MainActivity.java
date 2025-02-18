package com.example.kalkulasiapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
    }

    public void tambah(View view) {
        EditText input1 = findViewById(R.id.input1);
        EditText input2 = findViewById(R.id.input2);
        TextView output = findViewById(R.id.output);
        float number1 = Integer.parseInt(input1.getText().toString());
        float number2 = Integer.parseInt(input2.getText().toString());
        output.setText(String.valueOf(number1 + number2));
    }

    public void kurang(View view) {
        EditText input1 = findViewById(R.id.input1);
        EditText input2 = findViewById(R.id.input2);
        TextView output = findViewById(R.id.output);
        float number1 = Integer.parseInt(input1.getText().toString());
        float number2 = Integer.parseInt(input2.getText().toString());
        output.setText(String.valueOf(number1 - number2));
    }

    public void kali(View view) {
        EditText input1 = findViewById(R.id.input1);
        EditText input2 = findViewById(R.id.input2);
        TextView output = findViewById(R.id.output);
        float number1 = Integer.parseInt(input1.getText().toString());
        float number2 = Integer.parseInt(input2.getText().toString());
        output.setText(String.valueOf(number1 * number2));
    }

    public void bagi(View view) {
        EditText input1 = findViewById(R.id.input1);
        EditText input2 = findViewById(R.id.input2);
        TextView output = findViewById(R.id.output);
        float number1 = Integer.parseInt(input1.getText().toString());
        float number2 = Integer.parseInt(input2.getText().toString());
        output.setText(String.valueOf(number1 / number2));
    }

    public void persen(View view) {
        EditText input1 = findViewById(R.id.input1);
        EditText input2 = findViewById(R.id.input2);
        TextView output = findViewById(R.id.output);
        float number1 = Integer.parseInt(input1.getText().toString());
        float number2 = Integer.parseInt(input2.getText().toString());
        output.setText(String.valueOf((number1 / number2)*100+"%"));
    }
}