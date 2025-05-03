package com.malsoryz.tebakyo;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class StageMenu extends Fragment {

    private MainActivity activity;
    private Router router;
    private ImageButton backButton;
    private Button stageOneButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        assert activity != null;
        router = activity.router;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stage_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> router.navigateBack());
        stageOneButton = view.findViewById(R.id.stageOne);
        stageOneButton.setOnClickListener(v -> router.navigateTo(activity.gameplay, true));
    }
}