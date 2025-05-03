package com.malsoryz.tebakyo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public Router router;
    public final Handler handler = new Handler();
    public String dashboard = "Dashboard";
    public String stageMenu = "StageMenu";
    public String gameplay = "Gameplay";

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

        router = new Router(getSupportFragmentManager(), R.id.FragmentPoint, this);

        router.addRoute(dashboard, Dashboard.class);
        router.addRoute(stageMenu, StageMenu.class);
        router.addRoute(gameplay, Gameplay.class);

        if (savedInstanceState == null) {
            router.navigateTo(dashboard, false);
        }
    }

    public void makeDialog(String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", positive)
                .setNegativeButton("No", negative)
                .show();
    }

    public int stringToId(String target) {
        return Math.abs(target.hashCode() % Integer.MAX_VALUE);
    }
}