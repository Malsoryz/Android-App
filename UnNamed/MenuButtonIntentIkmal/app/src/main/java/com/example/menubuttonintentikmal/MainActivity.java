package com.example.menubuttonintentikmal;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
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
        findViewById(R.id.panggil).setOnClickListener(v -> {
            Intent Call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:085387261315"));
            startActivity(Call);
        });
        findViewById(R.id.youtube).setOnClickListener(v -> {
            Intent Youtube = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"));
            startActivity(Youtube);
        });
        findViewById(R.id.xapp).setOnClickListener(v -> {
            Intent x = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com"));
            startActivity(x);
        });
        findViewById(R.id.spotify).setOnClickListener(v -> {
            Intent spotify = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.spotify.com"));
            startActivity(spotify);
        });
        findViewById(R.id.github).setOnClickListener(v -> {
            Intent Github = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.github.com"));
            startActivity(Github);
        });
        findViewById(R.id.shopee).setOnClickListener(v -> {
            Intent shopee = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.shopee.co.id"));
            startActivity(shopee);
        });
        findViewById(R.id.chatgpt).setOnClickListener(v -> {
            Intent chatgpt = new Intent(Intent.ACTION_VIEW, Uri.parse("https://chatgpt.com"));
            startActivity(chatgpt);
        });
        findViewById(R.id.ig).setOnClickListener(v -> {
            Intent ig = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com"));
            startActivity(ig);
        });
        findViewById(R.id.discord).setOnClickListener(v -> {
            Intent discord = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.discord.com"));
            startActivity(discord);
        });
    }
}