package com.malsoryz.tebakyo;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Gameplay extends Fragment {

    private MainActivity action;
    private TextView CountdownView;
    private int countdownTime;
    private Runnable runnable;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        action = (MainActivity) getActivity();
        countdownTime = 30;
        runnable = initRunnable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gameplay, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CountdownView = view.findViewById(R.id.Countdown);
        action.handler.post(runnable);
        askBeforeLeave(runnable);
    }

    private Runnable initRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                countdownTime--;
                if (countdownTime >= 0) {
                    CountdownView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 256);
                    CountdownView.setText(String.valueOf(countdownTime + 1));
                    action.handler.postDelayed(this, 1000);
                } else {
                    CountdownView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 128);
                    CountdownView.setText("Start!");
                }
            }
        };
    }

    public void askBeforeLeave(Runnable runnable) {
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        action.handler.removeCallbacks(runnable);
                        action.makeDialog(
                                "Confirm",
                                "Membatalkan sesi permainan?",
                                (dialog, which) -> action.navigateBack(),
                                (dialog, which) -> {
                                    action.handler.post(runnable);
                                    dialog.dismiss();
                                }
                        );
                    }
                });
    }
}