package com.example.thermoconverter;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

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

    public void fromcelsius(View view) {
        Intent intent =  new Intent(MainActivity.this, fromCelsius.class);
        startActivity(intent);
    }

    public void fromfahrenheit(View view) {
        Intent intent =  new Intent(MainActivity.this, fromFahrenheit.class);
        startActivity(intent);
    }

    public void fromkelvin(View view) {
        Intent intent =  new Intent(MainActivity.this, fromKelvin.class);
        startActivity(intent);
    }

    public void fromreamur(View view) {
        Intent intent =  new Intent(MainActivity.this, fromReamur.class);
        startActivity(intent);
    }
}