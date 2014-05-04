package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.service;

import com.overnightApps.myapplication.app.core.Comment;
import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.LetterVote;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.LetterDao;
import com.overnightApps.myapplication.app.dao.LetterVoteDao;
import com.overnightApps.myapplication.app.dao.UserDao;
import com.overnightApps.myapplication.app.service.LetterVoter;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by andre on 4/10/14.
 */
public class LetterVoterTest extends TestCase {
    Letter letter;

    @Mock
    User userStub;

    @Mock
    LetterVote letterVote;

    @Mock
    LetterDao letterDao;

    @Mock
    LetterVoteDao letterVoteDao;

    @Mock
    UserDao userDao;

    private LetterVoter letterVoter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        letter = new Letter("recipient","message","signature",mock(User.class),9,new ArrayList<Comment>()
        ,mock(Letter.class),true,0);
        letterVoter = new LetterVoter(letter, userStub, letterVote, letterVoteDao, letterDao, userDao);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        letter = null;
        userStub = null;
    }

    public void test_VoteLetterUp_userHadNoVoteOnLetter_increaseInLetterScoreReturn1() throws Exception {
        letterVoter.setLetterVote(null);
        verifyLetterIncreasesAfterVote(1, true);
    }


    public void test_voteLetterUp_userHadVotedUpOnLetter_noChangeInLetterReturnNegative1() throws Exception {
        when(letterVote.getVoteType()).thenReturn(LetterVote.Vote.UP);
        verifyLetterIncreasesAfterVote(-1, true);
    }

    public void test_voteLetterUp_userHadRemovedVoteOnLetter_increaseInLetterScoreReturn1() throws Exception {
        when(letterVote.getVoteType()).thenReturn(LetterVote.Vote.NONE);
        verifyLetterIncreasesAfterVote(1, true);
    }

    public void test_voteLetterUp_userHadVotedDownOnLetter_increaseInLetterScoreReturn2() throws Exception {
        when(letterVote.getVoteType()).thenReturn(LetterVote.Vote.DOWN);
        verifyLetterIncreasesAfterVote(2, true);
    }

    public void test_VoteLetterDown_userHadNoVoteOnLetter_decreaseInLetterScoreReturnNegative1() throws Exception {
        letterVoter.setLetterVote(null);
        verifyLetterIncreasesAfterVote(-1, false);
    }

    public void test_VoteLetterDown_userHadVotedUpOnLetter_decreaseInLetterScoreReturnNegative2() throws Exception {
        when(letterVote.getVoteType()).thenReturn(LetterVote.Vote.UP);
        verifyLetterIncreasesAfterVote(-2, false);
    }

    public void test_VoteLetterDown_userHadRemovedVoteOnLetter_decreaseInLetterScoreReturnNegative1() throws Exception {
        when(letterVote.getVoteType()).thenReturn(LetterVote.Vote.NONE);
        verifyLetterIncreasesAfterVote(-1, false);
    }

    public void test_VoteLetterDown_userHadVotedDownOnLetter_increaseInLetterScoreReturn1() throws Exception {
        when(letterVote.getVoteType()).thenReturn(LetterVote.Vote.DOWN);
        verifyLetterIncreasesAfterVote(1, false);
    }

    public void test_saveChanges_userUpVotesALetter_saveFunctionCalledOnLetterDao() throws Exception {
        letterVoter.setLetterVote(null);
        letterVoter.voteLetterUp();
        letterVoter.saveChanges();
        verify(letterDao).save(letter);
    }

    public void test_saveChanges_userDoesNotVoteOnLetter_noSaveFunctionCalled() throws Exception {
        letterVoter.setLetterVote(null);
        letterVoter.saveChanges();
        verify(letterDao, never()).save(letter);
    }

    public void test_saveChanges_userVotesOnLetter_verifyThatTheLetterVoteIsSaved() throws Exception {
        letterVoter.setLetterVote(null);
        letterVoter.voteLetterUp();
        letterVoter.saveChanges();
        verify(letterVoteDao).save(any(LetterVote.class));
    }

    public void test_saveChanges_userDoesNotVoteOnLetter_verifyThatTheLetterIsNotSaved() throws Exception {
        letterVoter.setLetterVote(null);
        letterVoter.saveChanges();
        verify(letterVoteDao, never()).save(any(LetterVote.class));
    }

    private void verifyLetterIncreasesAfterVote(int increaseAmount, boolean isUpVote) {
        final int initialLetterVote = letter.getVotes();
        if(isUpVote) {
            letterVoter.voteLetterUp();
        }else{
            letterVoter.voteLetterDown();
        }
        final int finalLetterVotes = letter.getVotes();
        Assert.assertEquals(initialLetterVote + increaseAmount, finalLetterVotes);
    }
}