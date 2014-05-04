package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.service;

import com.overnightApps.myapplication.app.core.Comment;
import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.LetterDao;
import com.overnightApps.myapplication.app.service.PublicLetterSender;

import junit.framework.TestCase;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by andre on 5/1/14.
 */
public class PublicLetterSenderTest extends TestCase {
   public void test_sendLetter_letterIsSentPublicly_verifyThatTheLetterIsSavedInDao() throws Exception {
        final String recipients = "recipients";
        final String message = "message";
        final String PUBLIC_SIGNATURE = "publicSignature";

         User letterCreatorStub = mock(User.class);
         when(letterCreatorStub.getPublicSignature()).thenReturn(PUBLIC_SIGNATURE);

         Letter originalLetterStub = mock(Letter.class);
         LetterDao letterDaoMock = mock(LetterDao.class);

         PublicLetterSender publicLetterSender = new PublicLetterSender(originalLetterStub,
                    letterCreatorStub,letterDaoMock);

         publicLetterSender.sendLetter(recipients,message);

         Letter verifyThisLetterSent = new Letter(recipients,message,PUBLIC_SIGNATURE,letterCreatorStub,
                    0,new ArrayList<Comment>(),originalLetterStub,true);

         verify(letterDaoMock).save(verifyThisLetterSent);
        }
}
