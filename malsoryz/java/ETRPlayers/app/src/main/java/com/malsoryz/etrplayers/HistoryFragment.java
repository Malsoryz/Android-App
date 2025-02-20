package com.malsoryz.etrplayers;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class HistoryFragment extends Fragment {

    private ETRDatabase db;
    private Cursor cursor, getListView;
    private HistoryAdapter adapter;
    private LinearLayout whenListExist;
    private TextView whenListIsEmpty;
    private Button deleteHistory;
    private ListView historyList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ETRDatabase(getContext());
        cursor = db.getHistoryList();
        adapter = new HistoryAdapter(getContext(), cursor, 0);
    }

    private void init(View layout) {
        whenListExist = layout.findViewById(R.id.whenListExist);
        whenListIsEmpty = layout.findViewById(R.id.whenListIsEmpty);
        deleteHistory = layout.findViewById(R.id.deleteHistory);
        historyList = layout.findViewById(R.id.historyList);
        historyList.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_history, container, false);
        init(layout);
        historyList.setOnItemClickListener((parent, view, position, id) -> {
            getListView = (Cursor) parent.getItemAtPosition(position);
            if (getListView != null) {
                int videoId = getListView.getInt(getListView.getColumnIndexOrThrow(ETRDatabase.HISTORY_COLUMN_ID));
                String videoTitle = getListView.getString(getListView.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_TITLE));
                String videoCreator = getListView.getString(getListView.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_CREATOR));
                String videoDesc = getListView.getString(getListView.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_DESC));
                int videoPath = getListView.getInt(getListView.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_PATH));
                long lastPlayed = getListView.getLong(getListView.getColumnIndexOrThrow(ETRDatabase.HISTORY_COLUMN_LAST_PLAY_AT));

                Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
                intent.putExtra(ETRDatabase.HISTORY_COLUMN_ID, videoId);
                intent.putExtra(ETRDatabase.VIDEO_COLUMN_TITLE, videoTitle);
                intent.putExtra(ETRDatabase.VIDEO_COLUMN_CREATOR, videoCreator);
                intent.putExtra(ETRDatabase.VIDEO_COLUMN_DESC, videoDesc);
                intent.putExtra(ETRDatabase.VIDEO_COLUMN_PATH, videoPath);
                intent.putExtra(ETRDatabase.HISTORY_COLUMN_LAST_PLAY_AT, lastPlayed);
                startActivity(intent);
            }
        });
        deleteHistory.setOnClickListener(delete -> {
            db.clearHistory();
            requireActivity().recreate();
            Toast.makeText(getContext(), "Reloading...", Toast.LENGTH_SHORT).show();
        });
        ifAdapterEmpty();
        return layout;
    }

    private void ifAdapterEmpty() {
        if (adapter == null || adapter.getCount() == 0) {
            whenListIsEmpty.setVisibility(View.VISIBLE);
            whenListExist.setVisibility(View.GONE);
        }
    }
}