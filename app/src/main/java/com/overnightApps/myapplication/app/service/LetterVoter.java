package com.overnightApps.myapplication.app.service;

import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.LetterVote;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.LetterDao;
import com.overnightApps.myapplication.app.dao.LetterVoteDao;
import com.overnightApps.myapplication.app.dao.UserDao;
import com.overnightApps.myapplication.app.util.MyAssert;

import java.io.Serializable;

/**
 * Created by andre on 4/10/14.
 */
public class LetterVoter implements Serializable {
    public static final int USER_POINT_MULTIPLE = 5;
    private final Letter letter;
    private final User voter;
    private LetterVote letterVote;
    private LetterVoteDao letterVoteDao;
    private LetterDao letterDao;
    private UserDao userDao;
    private User letterCreator;

    private int initialLetterVoteCount;

    public static LetterVoter newInstance(Letter letter, User user, LetterVote letterVote){
        return new LetterVoter(letter,user,letterVote,LetterVoteDao.instance(),LetterDao.instance()
        , UserDao.instance());
    }

    public LetterVoter(Letter letter, User voter, LetterVote letterVote, LetterVoteDao letterVoteDao,
                       LetterDao letterDao, UserDao userDao) {
        this.letter = letter;
        this.voter = voter;
        this.letterVote = letterVote;
        this.letterVoteDao = letterVoteDao;
        this.letterDao = letterDao;
        this.userDao = userDao;

        this.letterCreator = letter.getCreator();
        this.initialLetterVoteCount = letter.getVotes();
    }

    public LetterVote.Vote getCurrentVote(){
        if(hasUserVotedOnLetter()){
            return null;
        }else{
            return letterVote.getVoteType();
        }
    }

    public void voteLetterUp() {
        int changeInLetterVote =  0;
        if(! hasUserVotedOnLetter()) {
            letterVote = new LetterVote(voter,letter, LetterVote.Vote.UP);
            changeInLetterVote = letterVote.getVoteValue();
        }else {
            LetterVote.Vote voteType = letterVote.getVoteType();
            if (voteType == LetterVote.Vote.UP) {
                letterVote.removeVote();
                changeInLetterVote = -1;
            } else if (voteType == LetterVote.Vote.DOWN) {
                letterVote.voteLetterDown();
                changeInLetterVote = 2;
            } else if (voteType == LetterVote.Vote.NONE) {
                letterVote.voteLetterUp();
                changeInLetterVote = 1;
            } else {
                MyAssert.assertShouldNotReachHere();
            }
        }
        applyChanges(changeInLetterVote);
    }

    public void voteLetterDown() {
        int changeInLetterVote = 0;
        if(! hasUserVotedOnLetter()) {
            letterVote = new LetterVote(voter,letter, LetterVote.Vote.DOWN);
            changeInLetterVote = letterVote.getVoteValue();
        }else {
            LetterVote.Vote voteType = letterVote.getVoteType();
            if (voteType == LetterVote.Vote.UP) {
                letterVote.voteLetterUp();
                changeInLetterVote = -2;
            } else if (voteType == LetterVote.Vote.DOWN) {
                letterVote.removeVote();
                changeInLetterVote = 1;
            } else if (voteType == LetterVote.Vote.NONE) {
                letterVote.voteLetterDown();
                changeInLetterVote = -1;
            } else {
                MyAssert.assertShouldNotReachHere();
            }
        }
        applyChanges(changeInLetterVote);
    }

    private void applyChanges(int changeInLetterVote) {
        letter.addAmountToRating(changeInLetterVote);
        letterCreator.addAmountToExperience(changeInLetterVote * USER_POINT_MULTIPLE);
    }

    private boolean hasUserVotedOnLetter() {
        return letterVote != null;
    }

    public void saveChanges(){
        if(isLetterCountChanged()){
            letterDao.save(letter);
            userDao.save(letterCreator);
        }
        if(letterVote != null) {
            letterVoteDao.save(letterVote);
        }
    }

    private boolean isLetterCountChanged(){
        return initialLetterVoteCount != letter.getVotes();
    }

    public void setLetterVote(LetterVote letterVote) {
        this.letterVote = letterVote;
    }
}