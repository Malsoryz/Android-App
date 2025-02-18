package com.malsoryz.etrplayers;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ETRDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        db = new ETRDatabase(this);
        db.recreateDatabase();
        try {
            db.addVideo(1,
                    "Never Gonna Give You Up",
                    "Rick Ashley",
                    "just a rickroll",
                    R.raw.rickroll,
                    ETRDatabase.getFrameFromVideo(this, R.raw.rickroll, 3000000)
            );
            db.addVideo(2,
                    "The Hour of Joy",
                    "Playtime Co",
                    "The special event in Playtime Co Factory",
                    R.raw.the_hour_of_joy,
                    ETRDatabase.getFrameFromVideo(this, R.raw.the_hour_of_joy, 4000000)
            );
            db.addVideo(3,
                    "Bukti Pemerintah belum peduli dengan produksi animasi lokal!!!",
                    "Portal Animasi",
                    "NOTE!! \"saya bukan pemilik dari clip, gambar maupun suara yang ada dalam video ini. saya menggunakan beberapa klip hanya untuk keperluan kritik dan review saja. Jika Anda ingin melihat pertunjukan dan episode secara lengkap. anda bisa cek di layanan streaming yang tersedia secara online.\"",
                    R.raw.pemerintah_tidak_peduli_dengan_animator,
                    ETRDatabase.getFrameFromVideo(this, R.raw.pemerintah_tidak_peduli_dengan_animator, 4000000)
            );
            db.addVideo(4,
                    "Absolutely normal cats",
                    "CAT-BRAINS.exe",
                    "Funny cat videos, cat videos, funny cats, funny cat videos 2025, cats funny videos, funny cat, funniest cats, cat video.",
                    R.raw.absolutely_normal_cats,
                    ETRDatabase.getFrameFromVideo(this, R.raw.absolutely_normal_cats, 26000000)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment setFragment = null;
            if (item.getItemId() == R.id.homeMenu) {
                setFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.historyMenu) {
                setFragment = new HistoryFragment();
            } else if (item.getItemId() == R.id.settingMenu) {
                setFragment = new SettingFragment();
            }

            if (setFragment != null) {
                loadFragment(setFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment f) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentPoint, f);
        transaction.commit();
    }
}