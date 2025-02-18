package com.malsoryz.malsplayers;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private String title, creator, duration,date;
    private List<Map<String, Object>> datatest;
    private Map<String, Object> item1, selectedVideo;
    private String[] from;
    private int[] to;
    private ListView videoList;
    private SimpleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datatest = new ArrayList<>();
        item1 = new HashMap<>();
        item1.put("title","Cara membuat Aplikasi by Malsoryzzz");
        item1.put("creator","Malsoryzzz");
        item1.put("duration","05:40");
        item1.put("date","2024/02/11");
        datatest.add(item1);

        from = new String[]{"title", "creator", "duration", "date"};
        to = new int[]{R.id.videoTitle, R.id.videoCreator, R.id.videoDuration, R.id.createdDate};

        adapter = new SimpleAdapter(
                getContext(),
                datatest,
                R.layout.video_list_item,
                from,
                to
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        videoList = rootView.findViewById(R.id.videoList);
        videoList.setAdapter(adapter);

        videoList.setOnItemClickListener((parent, view, position, id) -> {
            selectedVideo = (Map<String, Object>) parent.getItemAtPosition(position);
            title = (String) selectedVideo.get("title");
            creator = (String) selectedVideo.get("creator");
            duration = (String) selectedVideo.get("duration");
            date = (String) selectedVideo.get("date");

            Intent intent = new Intent(getContext(), ViewVideoActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("creator",creator);
            intent.putExtra("duration",duration);
            intent.putExtra("date",date);
            startActivity(intent);
        });

        return rootView;
    }
}