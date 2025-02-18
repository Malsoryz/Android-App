package com.eternity.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView minuteText, secondText, milisecondText;
    private ImageButton playButton, resetButton, addButton;
    private ListView listView;
    private Handler handler = new Handler();
    private boolean isRunning = false;
    private int minutes = 0, seconds = 0, miliseconds = 0;
    private List<String> timeList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize views
        minuteText = findViewById(R.id.minute);
        secondText = findViewById(R.id.second);
        milisecondText = findViewById(R.id.milisecond);
        playButton = findViewById(R.id.play);
        resetButton = findViewById(R.id.reset);
        addButton = findViewById(R.id.add);
        listView = findViewById(R.id.list);

        // Set ListView adapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.textcolor1));
                return view;
            }
        };
        listView.setAdapter(adapter);

        // Apply insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Start/pause stopwatch
        playButton.setOnClickListener(v -> {
            if (isRunning) {
                stopStopwatch();
            } else {
                startStopwatch();
            }
        });

        // Reset stopwatch and ListView
        resetButton.setOnClickListener(v -> resetStopwatch());

        // Add current time to ListView
        addButton.setOnClickListener(v -> addTimeToList());
    }

    private void startStopwatch() {
        isRunning = true;
        playButton.setImageResource(R.drawable.pause);
        handler.postDelayed(updateTimeRunnable, 10); // Update every 10ms
    }

    private void stopStopwatch() {
        isRunning = false;
        playButton.setImageResource(R.drawable.start);
        handler.removeCallbacks(updateTimeRunnable);
    }

    private void resetStopwatch() {
        stopStopwatch();
        minutes = 0;
        seconds = 0;
        miliseconds = 0;
        updateTextViews();
        timeList.clear();
        adapter.notifyDataSetChanged();
    }

    private void addTimeToList() {
        String currentTime = String.format("%02d:%02d:%02d", minutes, seconds, miliseconds);
        timeList.add(currentTime);
        adapter.notifyDataSetChanged();
    }

    private void updateTextViews() {
        minuteText.setText(String.format("%02d", minutes));
        secondText.setText(String.format("%02d", seconds));
        milisecondText.setText(String.format("%02d", miliseconds));
    }

    private final Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            miliseconds++;
            if (miliseconds == 100) {
                miliseconds = 0;
                seconds++;
            }
            if (seconds == 60) {
                seconds = 0;
                minutes++;
            }
            updateTextViews();
            handler.postDelayed(this, 10); // Repeat every 10ms
        }
    };
}
