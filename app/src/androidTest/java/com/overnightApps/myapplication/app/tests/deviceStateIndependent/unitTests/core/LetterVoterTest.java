package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.core;

import com.overnightApps.myapplication.app.core.LetterVote;

import junit.framework.TestCase;

/**
 * Created by andre on 3/22/14.
 */
public class LetterVoterTest extends TestCase {
    //test inner enum methods
    public void test_Vote_getKey_returnUp() throws Exception {
        assertEquals(LetterVote.Vote.UP, LetterVote.Vote.getVoteType(1));
    }

    public void test_Vote_getKey_returnDown() throws Exception {
        assertEquals(LetterVote.Vote.DOWN, LetterVote.Vote.getVoteType(-1));
    }

    public void test_Vote_getKey_returnNone() throws Exception {
        assertEquals(LetterVote.Vote.NONE, LetterVote.Vote.getVoteType(0));
    }
}
