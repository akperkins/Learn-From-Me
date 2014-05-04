package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.service;

import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.UserDao;
import com.overnightApps.myapplication.app.service.UserLogIn;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by andre on 3/28/14.
 */
public class UserLogInTest extends TestCase {
    @Mock
    UserDao userDaoStub;

    private UserLogIn userLogIn;

    private final String username = "rickGrimes87";
    private final String password = "Carl!!!!";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        userLogIn = new UserLogIn(username, password, userDaoStub);
    }

    public void test_logUserIn_successfulAttempt_returnsSuccessCode() throws Exception {
        User dummyUser = new User("","","",0,"","");
        when(userDaoStub.logIn(anyString(), anyString())).thenReturn(dummyUser);
        assertEquals(userLogIn.logUserIn(), UserLogIn.StatusCode.SUCCESS);
    }

    public void test_logUserIn_failedAttempt_returnsFailureCode() throws Exception {
        when(userDaoStub.logIn(anyString(), anyString())).thenReturn(null);
        assertEquals(userLogIn.logUserIn(), UserLogIn.StatusCode.FAILURE);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        userDaoStub = null;
        userLogIn = null;
    }
}
