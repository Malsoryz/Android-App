package com.malsoryz.etrplayers;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HomeFragment extends Fragment {

    private ListView videoList;
    private VideoAdapter adapter;
    private ETRDatabase db;
    private Cursor cursor, getListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ETRDatabase(getContext());
        cursor = db.getVideoList();
        adapter = new VideoAdapter(getContext(), cursor, 0);
    }

    private void init(View layout) {
        videoList = layout.findViewById(R.id.videoList);
        videoList.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_home, container, false);
        init(layout);
        videoList.setOnItemClickListener(((parent, view, position, id) -> {
            getListView = (Cursor) parent.getItemAtPosition(position);
            if (getListView != null) {
                int videoId = getListView.getInt(getListView.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_ID));
                String videoTitle = getListView.getString(getListView.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_TITLE));
                String videoCreator = getListView.getString(getListView.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_CREATOR));
                String videoDesc = getListView.getString(getListView.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_DESC));
                int videoPath = getListView.getInt(getListView.getColumnIndexOrThrow(ETRDatabase.VIDEO_COLUMN_PATH));

                Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
                intent.putExtra(ETRDatabase.VIDEO_COLUMN_ID, videoId);
                intent.putExtra(ETRDatabase.VIDEO_COLUMN_TITLE, videoTitle);
                intent.putExtra(ETRDatabase.VIDEO_COLUMN_CREATOR, videoCreator);
                intent.putExtra(ETRDatabase.VIDEO_COLUMN_DESC, videoDesc);
                intent.putExtra(ETRDatabase.VIDEO_COLUMN_PATH, videoPath);
                startActivity(intent);
            }
        }));
        return layout;
    }
}