package com.malsoryz.etrplayers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingFragment extends Fragment {

    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_setting, container, false);

        Button openGithub = layout.findViewById(R.id.openGithub);

        openGithub.setOnClickListener(git -> {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Malsoryz"));
            startActivity(intent);
        });

        return layout;
    }
}