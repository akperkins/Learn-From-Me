package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.core;

import com.overnightApps.myapplication.app.core.Comment;
import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.User;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by andre on 4/7/14.
 */
public class LetterTest extends TestCase {
    public void test_addComment_addValidMessage_messageAppendedToList() throws Exception {
        Letter letter = new Letter("","","",mock(User.class),0, new ArrayList<Comment>(), mock(Letter.class), true);
        final String message = "This is my new comment. I like to put sweet and sour sauce" +
                " on my french fries.";
        letter.addComment(message, mock(User.class));

        List<Comment> changedList = letter.getCommentList();
        final Comment lastComment= changedList.get(changedList.size() - 1);
        Assert.assertEquals(message, lastComment.getMessage());
    }

}