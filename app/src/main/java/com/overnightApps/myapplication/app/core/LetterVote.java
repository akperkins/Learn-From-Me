package com.overnightApps.myapplication.app.core;

import junit.framework.Assert;

import java.io.Serializable;

/**
 * Created by andre on 3/21/14.
 */
public class LetterVote implements Serializable {
    private Letter letter;
    private User user;
    private Vote voteType;

    public LetterVote(User user, Letter letter) {
        this.letter = letter;
        this.user = user;
        voteType = Vote.NONE;
    }

    public LetterVote(User user, Letter letter, Vote voteType) {
        this.user = user;
        this.letter = letter;
        this.voteType = voteType;
    }

    public Letter getLetter() {
        return letter;
    }

    public void setLetter(Letter letter) {
        this.letter = letter;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void voteLetterUp() {
        voteType = Vote.UP;
    }

    public void voteLetterDown() {
        voteType = Vote.DOWN;
    }

    public void removeVote() {
        voteType = Vote.NONE;
    }

    public int getVoteValue() {
        return voteType.value;
    }

    public Vote getVoteType() {
        return voteType;
    }

    public enum Vote {
        UP(1),
        DOWN(-1),
        NONE(0);
        private final int value;

        Vote(int value) {
            this.value = value;
        }

        public static Vote getVoteType(int value) {
            switch (value) {
                case 1:
                    return UP;
                case 0:
                    return NONE;
                case -1:
                    return DOWN;
                default:
                    Assert.assertNotNull("Should not have reached here", null);
                    return null;
            }
        }
    }
}
