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

import java.util.ArrayList;
import java.util.List;

public class VideoPlayerActivity extends AppCompatActivity {

    private ETRDatabase db;
    private Cursor cursor, getListView;
    private String videoTitle, videoCreator, videoDesc, shortDesc;
    private int videoId, videoSource;
    private long lastPlayed;
    private TextView viewTitle, viewCreator, viewDesc;
    private boolean isDescExpanded;
    private PlayerView videoPlayer;
    private ExoPlayer player;
    private ListView videoList;
    private VideoAdapter adapter;

    private String getStringExtraFromIntent(String key, String defaultValue) {
        Intent intent = new Intent(getIntent());
        return intent.hasExtra(key) ? intent.getStringExtra(key) : defaultValue;
    }

    private void init() {
        db = new ETRDatabase(this);
        isDescExpanded = false;

        //inisiasi dan mengambil data intent sebelumnya
        Intent getIntent = new Intent(getIntent());
        videoId = getIntent.getIntExtra(ETRDatabase.VIDEO_COLUMN_ID,0);
        videoTitle = getIntent.getStringExtra(ETRDatabase.VIDEO_COLUMN_TITLE);
        videoCreator = getIntent.getStringExtra(ETRDatabase.VIDEO_COLUMN_CREATOR);
        videoDesc = getIntent.getStringExtra(ETRDatabase.VIDEO_COLUMN_DESC);
        videoSource = getIntent.getIntExtra(ETRDatabase.VIDEO_COLUMN_PATH,0);
        lastPlayed = getIntent.getLongExtra(ETRDatabase.HISTORY_COLUMN_LAST_PLAY_AT, 0);

        //inisiasi info video
        viewTitle = findViewById(R.id.videoTitle);
        viewCreator = findViewById(R.id.videoCreator);
        viewDesc = findViewById(R.id.seeMoreDesc);
        shortDesc = videoDesc.length() > 50 ? videoDesc.substring(0, 50) + "..." : videoDesc;
        //setting info video
        viewTitle.setText(videoTitle);
        viewCreator.setText(videoCreator);
        viewDesc.setMaxLines(1);
        viewDesc.setText(shortDesc);

        //inisiasi listview
        cursor = db.getVideoListWithExclude(videoId);
        adapter = new VideoAdapter(this, cursor, 0);
        videoList = findViewById(R.id.videoList);
        videoList.setAdapter(adapter);

        //inisiasi PlayerView
        videoPlayer = findViewById(R.id.videoPlayer);
        player = new ExoPlayer.Builder(this).build();
        videoPlayer.setPlayer(player);
    }

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

        init();

        viewDesc.setOnClickListener(v -> expandDesc());

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoSource);
        playVideo(videoUri, lastPlayed);

        videoList.setOnItemClickListener((parent, view, position, id) -> {
            getListView = (Cursor) parent.getItemAtPosition(position);
            if (getListView != null) {
                int videoId = getListView.getInt(getListView.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_ID));
                String videoTitle = getListView.getString(getListView.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_TITLE));
                String videoCreator = getListView.getString(getListView.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_CREATOR));
                String videoDesc = getListView.getString(getListView.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_DESC));
                int videoPath = getListView.getInt(getListView.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_PATH));

                Intent intent = new Intent(this, VideoPlayerActivity.class);
                intent.putExtra(ETRDatabase.VIDEO_COLUMN_ID, videoId);
                intent.putExtra(ETRDatabase.VIDEO_COLUMN_TITLE, videoTitle);
                intent.putExtra(ETRDatabase.VIDEO_COLUMN_CREATOR, videoCreator);
                intent.putExtra(ETRDatabase.VIDEO_COLUMN_DESC, videoDesc);
                intent.putExtra(ETRDatabase.VIDEO_COLUMN_PATH, videoPath);
                startActivity(intent);
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getLastPlayed();
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

    private void expandDesc() {
        if (isDescExpanded) {
            viewDesc.setText(shortDesc);
            viewDesc.setMaxLines(1);
        } else {
            viewDesc.setText(videoDesc);
            viewDesc.setMaxLines(100);
        }
        isDescExpanded = !isDescExpanded;
    }

    private void getLastPlayed() {
        if (player != null) {
            long lastPlayed = player.getCurrentPosition();
            db.addHistory(videoId, lastPlayed);
        }
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
        getLastPlayed();
    }
}