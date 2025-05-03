package com.malsoryz.tebakyo;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

public class Gameplay extends Fragment {

    private MainActivity activity;
    private Router router;
    private TextView countdownView;
    private LinearLayout countdownContainer;

    // constant variable
    private final int QUESTION_DURATION = 20;
    private final int EXTRA_POINT_DURATION = 5;

    //game management
    private View gameplayLayout;
    private ProgressBar questionProgressBar;
    private ImageView questionImage;
    private TextView questionTextView, questionTimer;
    private LinearLayout questionButtonContainer, extraQuestionPoint;
    private List<Question> questionList;
    private int phase, currentQuestionIndex, currentCountdownDuration, currentQuestionDuration, correctAnswer, wrongAnswer = 0;
    private Runnable runnableCountdown, runnableQuestionTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        assert activity != null;
        router = activity.router;

        Question firstQuestion = new Question("What is the capital of Indonesia?", new String[]{"Jakarta", "Bandung", "Surabaya", "Banjarmasin"}, "Jakarta", 0);
        Question secondQuestion = new Question("What is the capital of Malaysia?", new String[]{"Jakarta", "Kuala Lumpur", "Surabaya", "New York"}, "Kuala Lumpur", 0);
        Question thirdQuestion = new Question("What is the capital of Thailand?", new String[]{"Jakarta", "Kuala Lumpur", "Bangkok", "Riyadh"}, "Bangkok", 0);
        questionList = new ArrayList<>();
        Collections.addAll(questionList, firstQuestion, secondQuestion, thirdQuestion);

        currentCountdownDuration = 3;
        currentQuestionDuration = QUESTION_DURATION;

        runnableCountdown = countdownBeforeStart();
        runnableQuestionTimer = startQuestionTimer();
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
        questionImage = gameplayLayout.findViewById(R.id.questionImage);
        questionTextView = gameplayLayout.findViewById(R.id.questionTextView);
        questionButtonContainer = gameplayLayout.findViewById(R.id.questionButtonContainer);
        extraQuestionPoint = gameplayLayout.findViewById(R.id.extraQuestionPoint);

        questionProgressBar.setMax(QUESTION_DURATION);
        questionProgressBar.setProgress(QUESTION_DURATION);

        countdownView = view.findViewById(R.id.countdownView);

        countdownContainer = view.findViewById(R.id.countdownContainer);

        activity.handler.post(runnableCountdown);

        Runnable[] runnables = {runnableCountdown, runnableQuestionTimer};
        askBeforeLeave(runnables);
    }

    private void mapAnswerButton(Question question) {
        for (String text : question.getOptions()) {
            View answerButtons = getLayoutInflater().inflate(R.layout.answer_button, questionButtonContainer);
            int getId = activity.stringToId(text);
            Button newButton = answerButtons.findViewById(R.id.answerButton);
            newButton.setText(text);
            newButton.setId(getId);
            newButton.setOnClickListener(v -> {
                if (currentQuestionDuration > 0) {
                    answerQuestion(text, question);
                } else if (currentQuestionDuration == 0) {
                    Toast.makeText(activity, "Telat", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Runnable countdownBeforeStart() {
        return new Runnable() {
            @Override
            public void run() {
                phase = 0;
                int delay = 750;
                currentCountdownDuration--;
                if (currentCountdownDuration >= 0) {
                    countdownView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 256);
                    countdownView.setText(String.valueOf(currentCountdownDuration + 1));
                    activity.handler.postDelayed(this, delay);
                } else {
                    countdownView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
                    countdownView.setText("Mulai!");
                    activity.handler.postDelayed(() -> countdownContainer.setVisibility(View.GONE), delay);
                    activity.handler.postDelayed(() -> {
                        phase = 1;
                        gameplayLayout.setVisibility(View.VISIBLE);
                        nextQuestion(questionList);
                    }, delay);
                    activity.handler.removeCallbacks(this);
                }
            }
        };
    }

    private Runnable startQuestionTimer() {
        return new Runnable() {
            @Override
            public void run() {
                int delay = 1000;
                currentQuestionDuration--;
                if (currentQuestionDuration >= 0) {
                    questionProgressBar.setProgress(currentQuestionDuration);
                    questionTimer.setText(String.valueOf(currentQuestionDuration));
                    if ((currentQuestionDuration) > (QUESTION_DURATION - EXTRA_POINT_DURATION)) {
                        extraQuestionPoint.setVisibility(View.VISIBLE);
                    } else extraQuestionPoint.setVisibility(View.GONE);
                    activity.handler.postDelayed(this, delay);
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

        questionProgressBar.setProgress(QUESTION_DURATION);
        Question question = questionList.get(currentQuestionIndex);
        currentQuestionDuration = QUESTION_DURATION;
        questionTextView.setText(question.getQuestion());
        questionButtonContainer.removeAllViews();
        mapAnswerButton(question);

        activity.handler.postDelayed(runnableQuestionTimer, 1000);
    }

    private void answerQuestion(String answer, Question question) {
        activity.handler.removeCallbacks(runnableQuestionTimer);

        String[] allQuestionButton = question.getOptions();

        for (String questionButton : allQuestionButton) {
            Button getButton = questionButtonContainer.findViewById(activity.stringToId(questionButton));
            if (questionButton.equals(answer)) {
                if (questionButton.equals(question.getCorrectAnswer())) {
                    getButton.setBackgroundColor(Color.parseColor("#00FF00"));
                } else getButton.setBackgroundColor(Color.parseColor("#FF0000"));
            } else getButton.setBackgroundColor(Color.parseColor("#0000FF"));
        }

        if (answer.equals(question.getCorrectAnswer())) {
            Toast.makeText(activity, "Benar", Toast.LENGTH_SHORT).show();
            correctAnswer++;
        } else {
            Toast.makeText(activity, "Salah", Toast.LENGTH_SHORT).show();
            wrongAnswer++;
        }

        currentQuestionIndex++;
        currentQuestionDuration = -1;
        activity.handler.postDelayed(() -> nextQuestion(questionList), 1000);
    }

    private void endGame() {
        phase = 2;
        gameplayLayout.setVisibility(View.GONE);
        countdownContainer.setVisibility(View.VISIBLE);
        int getCorrectAnswer = correctAnswer;
        int getWrongAnswer = wrongAnswer;
        countdownView.setText(String.format("%s %s", getCorrectAnswer, getWrongAnswer));
    }

    private void askBeforeLeave(Runnable[] runnable) {
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (phase != 2) {
                            for (Runnable run : runnable) {
                                activity.handler.removeCallbacks(run);
                            }
                            activity.makeDialog(
                                    "Confirm",
                                    "Membatalkan sesi permainan?",
                                    (dialog, which) -> router.navigateBack(),
                                    (dialog, which) -> {
                                        if (phase == 0) {
                                            activity.handler.post(runnableCountdown);
                                        } else if (phase == 1) {
                                            activity.handler.post(runnableQuestionTimer);
                                        }
                                        dialog.dismiss();
                                    }
                            );
                        } else router.navigateBack();
                    }
                });
    }
}