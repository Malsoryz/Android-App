package com.malsoryz.malsplayers;

import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;

public class ViewVideoActivity extends AppCompatActivity {

    private TextView getTitle, getCreator, getDuration, getDate;
    private String title, creator, duration,date;
    private PlayerView playerView;
    private ExoPlayer player;

    @OptIn(markerClass = UnstableApi.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        playerView = findViewById(R.id.videoViewer);
        player = new ExoPlayer.Builder(this).build();

        getTitle = findViewById(R.id.videoTitle);
        getCreator = findViewById(R.id.videoCreator);
        getDuration = findViewById(R.id.videoDuration);
        getDate = findViewById(R.id.createdDate);

        Intent getIntent = getIntent();

        title = getIntent.getStringExtra("title");
        creator = getIntent.getStringExtra("creator");
        duration = getIntent.getStringExtra("duration");
        date = getIntent.getStringExtra("date");

        Uri videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.rickroll);
        MediaItem mediaItem = new MediaItem.Builder().setUri(videoUri).build();
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        player.setMediaItem(mediaItem);

        player.prepare();
        player.play();

        getTitle.setText(title);
        getCreator.setText(creator);
        getDuration.setText(duration);
        getDate.setText(date);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
        }
    }
}