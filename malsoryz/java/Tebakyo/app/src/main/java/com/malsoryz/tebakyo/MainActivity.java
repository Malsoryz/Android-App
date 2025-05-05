package com.malsoryz.tebakyo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public Router router;
    public final Handler handler = new Handler();
    public String dashboard = "Dashboard";
    public String stageMenu = "StageMenu";
    public String gameplay = "Gameplay";
    public MediaPlayer backsound, buttonClicked, correctAnswerSound, wrongAnswerSound;

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
            router.navigateTo(dashboard, false, false);
            playBacksound(R.raw.main_backsound, 0.5f);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (backsound != null && backsound.isPlaying()) backsound.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (backsound != null && !backsound.isPlaying()) backsound.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backsound != null) {
            backsound.release();
            backsound = null;
        }
    }

    public void playBacksound(int musicFile, float volume) {
        if (backsound != null) backsound.release();
        backsound = MediaPlayer.create(this, musicFile);
        backsound.setLooping(true);
        backsound.setVolume(volume, volume);
        backsound.start();
    }

    public void clickSound() {
        if (buttonClicked != null) buttonClicked.release();
        buttonClicked = MediaPlayer.create(this, R.raw.button_click);
        buttonClicked.start();
    }

    public void correctSound() {
        if (correctAnswerSound != null) correctAnswerSound.release();
        correctAnswerSound = MediaPlayer.create(this, R.raw.correct_answer);
        correctAnswerSound.start();
    }

    public void wrongSound() {
        if (wrongAnswerSound != null) wrongAnswerSound.release();
        wrongAnswerSound = MediaPlayer.create(this, R.raw.wrong_answer);
        wrongAnswerSound.start();
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