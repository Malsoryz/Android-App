package com.malsoryz.tebakyo;

public class Question {
    private String question;
    private String[] options;
    private String correctAnswer;
    private int image;

    public Question(String question, String[] options, String correctAnswer, int image) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.image = image;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public int getImage() {
        return image;
    }
}
