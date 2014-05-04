package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.service;

import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.LetterRecommendation;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.LetterRecommendationDao;
import com.overnightApps.myapplication.app.service.RecommendationCreator;

import junit.framework.TestCase;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by andre on 5/1/14.
 */
public class RecommendationCreatorTest extends TestCase {
    public void test_sendToList_sendListTo2Users_verifyThat2RecommendationsAreSaved() throws Exception {
        LetterRecommendationDao letterRecommendationDaoMock = mock(LetterRecommendationDao.class);
        RecommendationCreator recommendationCreator = new RecommendationCreator(letterRecommendationDaoMock);

        User sender = mock(User.class);
        ArrayList<User> recipients = new ArrayList<>();
        recipients.add(mock(User.class));
        recipients.add(mock(User.class));
        Letter letter = mock(Letter.class);

        recommendationCreator.sendToList(sender, recipients, letter);

        verify(letterRecommendationDaoMock, times(2)).save(any(LetterRecommendation.class));
    }
}