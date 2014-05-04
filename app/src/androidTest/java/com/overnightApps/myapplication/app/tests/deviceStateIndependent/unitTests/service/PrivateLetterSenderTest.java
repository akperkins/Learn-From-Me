package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.service;

import com.overnightApps.myapplication.app.core.Comment;
import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.LetterDao;
import com.overnightApps.myapplication.app.service.PrivateLetterSender;

import junit.framework.TestCase;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by andre on 5/1/14.
 */
public class PrivateLetterSenderTest extends TestCase{
    public void test_sendLetter_letterIsSentPrivately_verifyThatTheLetterIsSavedInDao() throws Exception {
        final String recipients = "recipients";
        final String message = "message";
        final String PRIVATE_SIGNATURE = "privateSignature";

        User letterCreatorStub = mock(User.class);
        when(letterCreatorStub.getPrivateSignature()).thenReturn(PRIVATE_SIGNATURE);

        Letter originalLetterStub = mock(Letter.class);
        LetterDao letterDaoMock = mock(LetterDao.class);

        PrivateLetterSender privateLetterSender = new PrivateLetterSender(originalLetterStub,
                letterCreatorStub,letterDaoMock);

        privateLetterSender.sendLetter(recipients,message);

        Letter verifyThisLetterSent = new Letter(recipients,message,PRIVATE_SIGNATURE,letterCreatorStub,
                0,new ArrayList<Comment>(),originalLetterStub,false);

        verify(letterDaoMock).save(verifyThisLetterSent);
    }
}