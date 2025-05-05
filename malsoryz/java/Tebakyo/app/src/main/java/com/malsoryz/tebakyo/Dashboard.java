package com.malsoryz.tebakyo;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Dashboard extends Fragment {

    private MainActivity activity;
    private Router router;
    private Button PlayButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        assert activity != null;
        router = activity.router;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup menuButtonContainer = view.findViewById(R.id.menuButtonContainer);
        for (int i = 0; i < menuButtonContainer.getChildCount(); i++) {
            View button = menuButtonContainer.getChildAt(i);
            if (button instanceof Button) {
                button.setOnClickListener(v -> activity.clickSound());
            }
        }

        PlayButton = view.findViewById(R.id.PlayButton);
        PlayButton.setOnClickListener(v -> router.navigateTo(activity.stageMenu, true, true));
    }
}