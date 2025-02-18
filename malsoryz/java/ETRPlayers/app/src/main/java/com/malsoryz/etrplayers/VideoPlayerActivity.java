package com.malsoryz.etrplayers;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class VideoPlayerActivity extends AppCompatActivity {

    private PlayerView videoPlayer;
    private ExoPlayer player;
    private boolean isDescExpanded = false;
    private ETRDatabase db;
    private Cursor cursor;
    private VideoAdapter adapter;
    private int videoId, videoSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new ETRDatabase(this);
        cursor = db.getVideoList();
        adapter = new VideoAdapter(this, cursor, 0);

        Intent getIntent = new Intent(getIntent());
        videoId = getIntent.getIntExtra(ETRDatabase.VIDEO_COLUMN_ID,0);
        String videoTitle = getIntent.getStringExtra(ETRDatabase.VIDEO_COLUMN_TITLE);
        String videoCreator = getIntent.getStringExtra(ETRDatabase.VIDEO_COLUMN_CREATOR);
        String videoDesc = getIntent.getStringExtra(ETRDatabase.VIDEO_COLUMN_DESC);
        videoSource = getIntent.getIntExtra(ETRDatabase.VIDEO_COLUMN_PATH,0);
        long lastPlayed = getIntent.getLongExtra(ETRDatabase.HISTORY_COLUMN_LAST_PLAY_AT, 0);

        TextView viewTitle = findViewById(R.id.videoTitle);
        viewTitle.setText(videoTitle);
        TextView viewCreator = findViewById(R.id.videoCreator);
        viewCreator.setText(videoCreator);
        TextView viewDesc = findViewById(R.id.seeMoreDesc);
        viewDesc.setMaxLines(1);
        String shortDesc = videoDesc.length() > 50 ? videoDesc.substring(0, 50) + "..." : videoDesc;
        viewDesc.setText(shortDesc);

        viewDesc.setOnClickListener(v -> {
            if (isDescExpanded) {
                viewDesc.setText(shortDesc);
                viewDesc.setMaxLines(1);
            } else {
                viewDesc.setText(videoDesc);
                viewDesc.setMaxLines(100);
            }

            isDescExpanded = !isDescExpanded;
        });

        ListView videoList = findViewById(R.id.videoList);
        videoList.setAdapter(adapter);

        videoPlayer = findViewById(R.id.videoPlayer);
        player = new ExoPlayer.Builder(this).build();
        videoPlayer.setPlayer(player);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoSource);
        playVideo(videoUri, lastPlayed);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (player != null) {
                    long lastPlayed = player.getCurrentPosition();
                    db.addHistory(videoId,lastPlayed);
                }
                finish();
            }
        });
    }

    private void playVideo(Uri videoUri, long lastPlayed) {
        MediaItem mediaItem = new MediaItem.Builder().setUri(videoUri).build();
        player.setMediaItem(mediaItem);
        player.prepare();
        player.seekTo(lastPlayed);
        player.setPlayWhenReady(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            long lastPlayed = player.getCurrentPosition();
            db.addHistory(videoId,lastPlayed);
        }
    }
}