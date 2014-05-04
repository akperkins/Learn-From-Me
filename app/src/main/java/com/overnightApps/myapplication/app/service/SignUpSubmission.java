package com.overnightApps.myapplication.app.service;

import android.graphics.Bitmap;
import android.util.Log;

import com.overnightApps.myapplication.app.core.helper.SignUpForm;
import com.overnightApps.myapplication.app.dao.exceptions.UnableToSignUpException;
import com.overnightApps.myapplication.app.dao.UserDao;

import java.io.Serializable;

/**
 * Created by andre on 2/28/14.
 */
public class SignUpSubmission implements Serializable {
    private SignUpForm signUpForm;
    private UserDao userDao;
    private Bitmap userProfilePicture;

    public static SignUpSubmission newInstance(SignUpForm signUpForm, Bitmap userProfilePicture){
        return new SignUpSubmission(signUpForm,userProfilePicture,UserDao.instance());
    }

    public SignUpSubmission(SignUpForm signUpForm, Bitmap userProfilePicture, UserDao userDao) {
        this.signUpForm = signUpForm;
        this.userDao = userDao;
        this.userProfilePicture = userProfilePicture;
    }

    public boolean signUp() {
        try {
            userDao.signUserUp(signUpForm, userProfilePicture);
            return true;
        } catch (UnableToSignUpException e) {
            Log.e("SignUpSubmission", "Error while signing user up on back-end",e);
            return false;
        }
    }

    public void setDao(UserDao dao) {
        this.userDao = dao;
    }
}
