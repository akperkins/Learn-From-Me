package com.overnightApps.myapplication.app.service;

import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.UserDao;
import com.overnightApps.myapplication.app.dao.exceptions.DataClassNotFoundException;
import com.overnightApps.myapplication.app.dao.exceptions.SavedUserIsNotFoundOnBackEndException;

import junit.framework.Assert;

import java.io.Serializable;


/**
 * Created by andre on 3/25/14.
 */
public class UserSession implements Serializable {
    private User currentUser;
    private final UserDao userDao;
    private boolean isRestoreCurrentUserFunctionCalled;

    public static UserSession newInstance(){
        return new UserSession(UserDao.instance(),null,false);
    }

    public UserSession(UserDao userDao, User currentUser, boolean isRestoreCurrentUserFunctionCalled) {
        this.userDao = userDao;
        this.currentUser = currentUser;
        this.isRestoreCurrentUserFunctionCalled = isRestoreCurrentUserFunctionCalled;
    }

    public User getCurrentUser() {
        Assert.assertTrue(isRestoreCurrentUserFunctionCalled);
        return currentUser;
    }

    public void restoreSavedUser() throws SavedUserIsNotFoundOnBackEndException {
        try {
            User user = userDao.getSavedUser();
            currentUser = userDao.searchForUserByEmail(user.getEmail());
        } catch (DataClassNotFoundException dataClassNotFoundException){
            currentUser = null;
        }finally {
            isRestoreCurrentUserFunctionCalled = true;
        }
    }

    public boolean isUserLoggedIn(){
        return currentUser != null;
    }

    public void logCurrentUserOut() {
        userDao.logUserOut();
    }
}