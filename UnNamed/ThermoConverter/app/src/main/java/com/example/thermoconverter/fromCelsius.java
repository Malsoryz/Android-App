package com.example.thermoconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class fromCelsius extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_from_celsius);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void convert(View view) {
        EditText input = findViewById(R.id.input);
        TextView outputfahrenheit = findViewById(R.id.outputfahrenheit);
        TextView outputkelvin = findViewById(R.id.outputkelvin);
        TextView outputreamur = findViewById(R.id.outputreamur);
        TextView errorOnInput = findViewById(R.id.inputerror);
        TextView errorOnBtn = findViewById(R.id.btnerror);
        String inputText = input.getText().toString().trim();
        if (inputText.isEmpty()){
            errorOnInput.setText("Insert Value!");
            errorOnInput.setVisibility(View.VISIBLE);
            errorOnBtn.setText("Input is empty!");
            errorOnBtn.setVisibility(View.VISIBLE);
            outputfahrenheit.setText("0");
            outputkelvin.setText("0");
            outputreamur.setText("0");
        }
        if (!inputText.isEmpty()){
            try {
                errorOnInput.setVisibility(View.GONE);
                errorOnBtn.setVisibility(View.GONE);
                float parseInput = Float.parseFloat(inputText);
                float fahrenheit = (parseInput * 9 / 5) + 32;
                float kelvin = parseInput + 273.15f;
                float reamur = parseInput * 4 / 5;
                outputfahrenheit.setText(String.format("%.2f °F", fahrenheit));
                outputkelvin.setText(String.format("%.2f K", kelvin));
                outputreamur.setText(String.format("%.2f °R", reamur));
            }
            catch (NumberFormatException e) {
                errorOnInput.setText("Invalid!");
                errorOnInput.setVisibility(View.VISIBLE);
                errorOnBtn.setText("Input invalid!");
                errorOnBtn.setVisibility(View.VISIBLE);
                outputfahrenheit.setText("0");
                outputkelvin.setText("0");
                outputreamur.setText("0");
            }
        }
    }
}