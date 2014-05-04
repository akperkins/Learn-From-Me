package com.overnightApps.myapplication.app.core;

/**
 * Created by andre on 4/11/14.
 */
public class LetterRecommendation {
    private User to;
    private User from;
    private Letter letter;

    public LetterRecommendation(User to, User from, Letter letter) {
        this.to = to;
        this.from = from;
        this.letter = letter;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public Letter getLetter() {
        return letter;
    }

    public void setLetter(Letter letter) {
        this.letter = letter;
    }
}
