package com.malsoryz.tebakyo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public final Handler handler = new Handler();
    public String Dashboard = "Dashboard";
    public String StageMenu = "StageMenu";
    public String Gameplay = "Gameplay";
    public HashMap<String, Fragment> route = new HashMap<>();;

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
        route.put(Dashboard, new Dashboard());
        route.put(StageMenu, new StageMenu());
        route.put(Gameplay, new Gameplay());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentPoint, Objects.requireNonNull(route.get(Dashboard)))
                .commit();
    }

    public void navigateTo(String routeName) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentPoint, Objects.requireNonNull(route.get(routeName)))
                .addToBackStack(null)
                .commit();
    }
    public void navigateBack() {
        getSupportFragmentManager().popBackStack();
    }

    public void makeDialog(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", positive)
                .setNegativeButton("No", negative)
                .show();
    }
}