package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.service;

import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.UserDao;
import com.overnightApps.myapplication.app.dao.exceptions.DataClassNotFoundException;
import com.overnightApps.myapplication.app.service.UserSession;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by andre on 5/1/14.
 */
public class UserSessionTest extends TestCase{
    UserSession userSession;

    @Mock
    UserDao userDaoStub;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        userSession = new UserSession(userDaoStub, null, false);
    }

    public void test_getCurrentUser_calledBeforeUserInitialized_throwsAssertionError() throws Exception {
        boolean assertionThrown;
        try{
            userSession.getCurrentUser();
            assertionThrown = false;
        } catch (AssertionError assertionError){
            assertionThrown = true;
        }
        Assert.assertTrue(assertionThrown);
    }

    public void test_restoreSavedUser_thereIsNoUserSaved_CurrentUserSetToNull() throws Exception {
        when(userDaoStub.getSavedUser()).thenThrow(new DataClassNotFoundException());
        userSession.restoreSavedUser();
        assertNull(userSession.getCurrentUser());
    }

    public void test_restoreSavedUser_aUserIsSaved_CurrentUserIsNotNull() throws Exception {
        User user = new User("email","fullname","t",0,"public","private");
        when(userDaoStub.getSavedUser()).thenReturn(user);
        when(userDaoStub.searchForUserByEmail(anyString())).thenReturn(user);
        userSession.restoreSavedUser();
        assertEquals(user, userSession.getCurrentUser());
    }

    public void test_logCurrentUserOut_userIsLoggedIn_verifyThatTheLogOutFunctionIsCalledOnDao() throws Exception {
        userSession.logCurrentUserOut();
        verify(userDaoStub).logUserOut();
    }

    public void test_isUserLoggedIn_userIsLoggedOut_returnFalse() throws Exception {
        userSession = new UserSession(userDaoStub,null,false);
        assertFalse(userSession.isUserLoggedIn());
    }

    public void test_isUserLoggedIn_userIsLoggedIn_returnTrue() throws Exception {
        userSession = new UserSession(userDaoStub, mock(User.class), true);
        assertTrue(userSession.isUserLoggedIn());
    }
}