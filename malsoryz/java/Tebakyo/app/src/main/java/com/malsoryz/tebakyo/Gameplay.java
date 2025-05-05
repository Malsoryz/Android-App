package com.malsoryz.tebakyo;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Gameplay extends Fragment {

    private MainActivity activity;
    private Router router;

    private TextView countdownView;
    private LinearLayout countdownContainer;

    // constant variable
    private final int QUESTION_DURATION = 30;
    private final int EXTRA_POINT_DURATION = 10;
    private final int COUNTDOWN_INTERVAL = 750;
    private final int QUESTION_TIMER_INTERVAL = 1000;

    //game management
    private View gameplayLayout;
    private ProgressBar questionProgressBar;
    private ImageView questionImage;
    private TextView questionTextView, questionTimer, questionIndex, extrapointTimer;
    private LinearLayout questionButtonContainer, extraQuestionPoint;
    private List<Question> questionList;
    private int phase, currentQuestionIndex, currentQuestionDuration, currentExtraPointDuration, correctAnswer, wrongAnswer = 0;
    private int currentCountdownDuration = 3;
    private long point = 0;
    private Runnable runnableCountdown, runnableQuestionTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        assert activity != null;
        router = activity.router;

        Question firstQuestion = new Question("What is the capital of Indonesia?", new String[]{"Jakarta", "Bandung", "Surabaya", "Banjarmasin"}, "Jakarta", 0);
        Question secondQuestion = new Question("What is the capital of Malaysia?", new String[]{"Jakarta", "Kuala Lumpur", "Surabaya", "New York"}, "Kuala Lumpur", 1);
        Question thirdQuestion = new Question("What is the capital of Thailand?", new String[]{"Jakarta", "Kuala Lumpur", "Bangkok", "Riyadh"}, "Bangkok", 0);
        questionList = new ArrayList<>();
        Collections.addAll(questionList, firstQuestion, secondQuestion, thirdQuestion);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gameplay, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gameplayLayout = getLayoutInflater().inflate(R.layout.gameplay_layout, view.findViewById(R.id.inflaterPoint));
        gameplayLayout.setVisibility(View.GONE);

        questionProgressBar = gameplayLayout.findViewById(R.id.questionProgressBar);
        questionTimer = gameplayLayout.findViewById(R.id.questionTimer);
        questionIndex = gameplayLayout.findViewById(R.id.questionIndex);
        extrapointTimer = gameplayLayout.findViewById(R.id.extrapointTimer);
        questionImage = gameplayLayout.findViewById(R.id.questionImage);
        questionTextView = gameplayLayout.findViewById(R.id.questionTextView);
        questionButtonContainer = gameplayLayout.findViewById(R.id.questionButtonContainer);
        extraQuestionPoint = gameplayLayout.findViewById(R.id.extraQuestionPoint);

        questionProgressBar.setMax(QUESTION_DURATION);
        questionProgressBar.setProgress(QUESTION_DURATION);

        countdownView = view.findViewById(R.id.countdownView);
        countdownContainer = view.findViewById(R.id.countdownContainer);

        startGame();
        askBeforeLeave(new Runnable[]{runnableCountdown, runnableQuestionTimer});
    }

    private void startGame() {
        activity.playBacksound(R.raw.gameplay_backsound, activity.GAMEPLAY_SOUND);
        runnableCountdown = countdownBeforeStart();
        runnableQuestionTimer = startQuestionTimer();
        activity.handler.post(runnableCountdown);
    }

    private void mapAnswerButton(Question question) {
        for (String text : question.getOptions()) {
            View answerButtons = getLayoutInflater().inflate(R.layout.answer_button, questionButtonContainer);
            int getId = activity.stringToId(text);
            MaterialButton newButton = answerButtons.findViewById(R.id.answerButton);
            newButton.setText(text);
            newButton.setId(getId);
            newButton.setOnClickListener(v -> {
                if (currentQuestionDuration > 0) answerQuestion(text, question);
                else if (currentQuestionDuration == 0) Toast.makeText(activity, "Telat", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private Runnable countdownBeforeStart() {
        phase = 0;
        countdownView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 256);
        countdownView.setText(String.valueOf(currentCountdownDuration));
        return new Runnable() {
            @Override
            public void run() {
                currentCountdownDuration--;
                if (currentCountdownDuration >= 0) {
                    countdownView.setText(String.valueOf(currentCountdownDuration + 1));
                    activity.handler.postDelayed(this, COUNTDOWN_INTERVAL);
                } else {
                    countdownView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
                    countdownView.setText("Mulai!");
                    activity.handler.postDelayed(() -> countdownContainer.setVisibility(View.GONE), COUNTDOWN_INTERVAL);
                    activity.handler.postDelayed(() -> {
                        gameplayLayout.setVisibility(View.VISIBLE);
                        nextQuestion(questionList);
                    }, COUNTDOWN_INTERVAL);
                    activity.handler.removeCallbacks(this);
                }
            }
        };
    }

    private Runnable startQuestionTimer() {
        return new Runnable() {
            @Override
            public void run() {
                currentQuestionDuration--;
                if (currentQuestionDuration >= 0) {
                    questionProgressBar.setProgress(currentQuestionDuration);
                    questionProgressBar.setProgressDrawable(ContextCompat.getDrawable(requireContext(),
                                    (currentQuestionDuration) > (QUESTION_DURATION - EXTRA_POINT_DURATION)
                                            ? R.drawable.progressbar_style_extrapoint
                                            : R.drawable.progressbar_style
                            )
                    );
                    questionTimer.setText(String.format("%ss", currentQuestionDuration));
                    questionIndex.setText(String.format("%s/%s", currentQuestionIndex + 1, questionList.size()));
                    extrapointTimer.setText(String.format("%ss", currentQuestionDuration - (QUESTION_DURATION - EXTRA_POINT_DURATION)));
                    if ((currentQuestionDuration) > (QUESTION_DURATION - EXTRA_POINT_DURATION)) currentExtraPointDuration--;
                    else activity.handler.postDelayed(() -> extraQuestionPoint.setVisibility(View.GONE), 1000);
                    activity.handler.postDelayed(this, QUESTION_TIMER_INTERVAL);
                } else {
                    questionProgressBar.setProgress(0);
                    questionTimer.setText("Waktu habis");
                    currentQuestionDuration = 0;
                    currentQuestionIndex++;
                    wrongAnswer++;
                    activity.handler.removeCallbacks(this);
                    activity.handler.postDelayed(() -> nextQuestion(questionList), 1000);
                }
            }
        };
    }

    private void nextQuestion(List<Question> questionList) {
        if (currentQuestionIndex >= questionList.size()) {
            endGame();
            return;
        }

        phase = 1;
        currentQuestionDuration = QUESTION_DURATION;
        currentExtraPointDuration = EXTRA_POINT_DURATION;
        Question question = questionList.get(currentQuestionIndex);

        questionProgressBar.setProgress(QUESTION_DURATION);
        questionProgressBar.setProgressDrawable(ContextCompat.getDrawable(requireContext(),
                (currentQuestionDuration) > (QUESTION_DURATION - EXTRA_POINT_DURATION)
                        ? R.drawable.progressbar_style_extrapoint
                        : R.drawable.progressbar_style
                )
        );
        questionTimer.setText(String.format("%ss", currentQuestionDuration));
        questionIndex.setText(String.format("%s/%s", currentQuestionIndex + 1, questionList.size()));
        extrapointTimer.setText(String.format("%ss", currentQuestionDuration - (QUESTION_DURATION - EXTRA_POINT_DURATION)));
        extraQuestionPoint.setVisibility(View.VISIBLE);
        questionImage.setVisibility(question.getImage() == 0 ? View.GONE : View.VISIBLE);
        questionTextView.setText(question.getQuestion());
        questionButtonContainer.removeAllViews();
        mapAnswerButton(question);

        activity.handler.postDelayed(runnableQuestionTimer, 1000);
    }

    private void answerQuestion(String answer, Question question) {
        activity.handler.removeCallbacks(runnableQuestionTimer);

        double normalPoint = (currentQuestionDuration <= (QUESTION_DURATION - EXTRA_POINT_DURATION)) ? currentQuestionDuration * ((double) 100 / (QUESTION_DURATION - EXTRA_POINT_DURATION)) : 100;
        double extraPoint = 3 * currentExtraPointDuration;
        long fullPoint = Math.round(normalPoint + extraPoint);
        for (String questionButton : question.getOptions()) {
            Button getButton = questionButtonContainer.findViewById(activity.stringToId(questionButton));
            int buttonColor = ContextCompat.getColor(requireContext(),
                    questionButton.equals(question.getCorrectAnswer())
                            ? R.color.correct_color
                            : R.color.wrong_color );
            long pointValue = questionButton.equals(question.getCorrectAnswer()) ? fullPoint : 0;

            if (questionButton.equals(answer)) {
                activity.answerSound(questionButton.equals(question.getCorrectAnswer()));
                getButton.setBackgroundColor(buttonColor);
                point += pointValue;
                if (questionButton.equals(question.getCorrectAnswer())) correctAnswer++;
                else wrongAnswer++;
            } else getButton.setAlpha(0.5f);
        }

        currentQuestionIndex++;
        currentQuestionDuration = -1;
        activity.handler.postDelayed(() -> nextQuestion(questionList), 1000);
    }

    private void endGame() {
        phase = 2;
        gameplayLayout.setVisibility(View.GONE);
        countdownContainer.setVisibility(View.VISIBLE);
        countdownView.setText(String.format("%s %s %s", correctAnswer, wrongAnswer, point));
        activity.playBacksound(R.raw.result_backsound, activity.RESULT_SOUND);
    }

    private void askBeforeLeave(Runnable[] runnable) {
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (phase != 2) {
                            for (Runnable run : runnable) activity.handler.removeCallbacks(run);
                            activity.makeDialog(
                                    "Confirm",
                                    "Membatalkan sesi permainan?",
                                    (dialog, which) -> {
                                        router.navigateBack();
                                        activity.playBacksound(R.raw.main_backsound, activity.MAIN_SOUND);
                                    },
                                    (dialog, which) -> {
                                        if (phase == 0) activity.handler.post(runnableCountdown);
                                        else if (phase == 1) activity.handler.post(runnableQuestionTimer);
                                        dialog.dismiss();
                                    }
                            );
                        } else {
                            router.navigateBack();
                            activity.playBacksound(R.raw.main_backsound, activity.MAIN_SOUND);
                        }
                    }
                });
    }
}