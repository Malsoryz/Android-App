package com.eternity.bystro;

import static com.eternity.bystro.BystroDatabase.CREATE_ADDRESS_TABLE;
import static com.eternity.bystro.BystroDatabase.CREATE_ORDER_TABLE;
import static com.eternity.bystro.BystroDatabase.CREATE_PRODUCT_LIST_TABLE;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    private String[][] productlist = {
            {"Glidex Phantom", "GX-300", "249000", String.valueOf(R.drawable.mouse1), "Glidex Phantom GX-300 adalah mouse gaming ergonomis dengan desain futuristik yang memadukan kenyamanan dan performa tinggi. Mouse ini dirancang khusus untuk gamer yang membutuhkan akurasi, kecepatan, dan daya tahan dalam sesi permainan panjang. Dengan pencahayaan LED biru pada scroll wheel dan logo, GX-300 tidak hanya berfungsi dengan baik, tetapi juga terlihat memukau di meja Anda.\n\nTombol tambahan di sisi samping memberikan akses cepat untuk fungsi shortcut atau kontrol dalam game, sehingga meningkatkan respons dan efisiensi. Dibuat dengan material matte berkualitas tinggi, mouse ini memberikan genggaman yang nyaman sekaligus tahan lama.", String.valueOf(129)},
            {"Auraluxe", "Zenith 500", "335000", String.valueOf(R.drawable.headsetgood), "Auraluxe Zenith 500 adalah headphone premium yang dirancang untuk pecinta musik sejati yang menginginkan pengalaman audio imersif dan desain modern. Dengan kombinasi warna ungu metalik dan hitam matte, headphone ini tidak hanya memberikan performa luar biasa, tetapi juga menghadirkan estetika yang memukau. Cocok untuk digunakan dalam berbagai kondisi, baik saat santai di rumah, bepergian, maupun bekerja.\n\nEar cup besar dengan bantalan kulit sintetis berkualitas tinggi memastikan kenyamanan bahkan setelah berjam-jam pemakaian. Headband fleksibel memberikan penyesuaian optimal untuk berbagai ukuran kepala tanpa mengorbankan daya tahan.\n\nTombol kontrol pada ear cup kanan memudahkan pengguna untuk mengatur volume, memutar/menghentikan musik, atau menerima panggilan tanpa perlu mengakses perangkat utama. Ditambah dengan mikrofon bawaan, Zenith 500 juga ideal untuk panggilan telepon atau konferensi virtual.", String.valueOf(150)},
            {"KZ ZSX Terminator", "In-Ear Monitor (IEM) Hybrid","145000",String.valueOf(R.drawable.headset1), "KZ ZSX Terminator adalah IEM generasi baru yang menggabungkan teknologi hybrid dengan desain futuristik. Dibekali dengan 6 driver (1 dynamic + 5 balanced armature), ZSX menawarkan detail suara yang mengesankan, dengan bass yang kuat, vokal jernih, dan treble yang tajam. Material casing berbahan resin dan logam menciptakan estetika premium sekaligus memastikan durabilitas tinggi. Kabelnya yang dapat dilepas menggunakan konektor 2-pin standar, memudahkan pengguna untuk melakukan upgrade. Ideal untuk audiophile maupun musisi, ZSX memberikan pengalaman audio yang mendalam dan profesional.",String.valueOf(200)},
            {"SSD Samsung EVO", "SATA 3.0", "630000", String.valueOf(R.drawable.ssdsamsung), "Samsung 870 EVO adalah SSD SATA berkapasitas 500GB yang dirancang untuk memberikan performa terbaik di kelasnya. Menggunakan teknologi V-NAND terbaru dan kontroler in-house dari Samsung, SSD ini menawarkan kecepatan baca hingga 560 MB/s dan tulis hingga 530 MB/s, memastikan waktu booting yang cepat, transfer data yang mulus, dan respons aplikasi yang lebih baik.\n\nDengan desain 2.5 inci, 870 EVO kompatibel dengan berbagai perangkat, mulai dari laptop hingga PC desktop. Dilengkapi dengan software Samsung Magician, pengguna dapat memantau kesehatan drive, mengoptimalkan performa, dan melakukan update firmware dengan mudah.\n\n870 EVO juga memiliki daya tahan yang luar biasa, mendukung hingga 300 TBW (Terabytes Written) dan garansi terbatas hingga 5 tahun, sehingga cocok untuk kebutuhan penyimpanan data jangka panjang, gaming, atau pekerjaan profesional.", String.valueOf(300)}
    };
    private String[][] productlistwithid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        ImageView bystrohome = findViewById(R.id.bystrohome);
        if (isDarkMode()) {
            bystrohome.setImageResource(R.drawable.bystro_home_dark);
        } else {
            bystrohome.setImageResource(R.drawable.bystro_home);
        }

        BystroDatabase bystrodb = new BystroDatabase(this);

        ImageButton aboutcreator = findViewById(R.id.aboutcreator);
        aboutcreator.setOnClickListener(view -> {
            Intent Github = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.github.com/Malsoryz"));
            startActivity(Github);
        });

        productlistwithid = new String[productlist.length][productlist[0].length + 1];
        for (int i = 0; i < productlist.length; i++) {
            productlistwithid[i][0] = String.valueOf(i + 1);
            System.arraycopy(productlist[i], 0, productlistwithid[i], 1, productlist[i].length);
        }

        bystrodb.addProduct(productlistwithid);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.order) {
                selectedFragment = new OrdersFragment();
            } else if (item.getItemId() == R.id.profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }
    public String[][] getProductlistwithid() {
        return productlistwithid;
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }
    public static String formatIntToRP(int number) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id","ID"));
        return formatter.format(number).replace(",00","");
    }
    private boolean isDarkMode() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}
