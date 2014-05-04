package com.overnightApps.myapplication.app.service;

import android.graphics.Bitmap;

import com.overnightApps.myapplication.app.core.helper.SignUpForm;

public class SignUpSubmissionBuilder {
    SignUpForm signUpForm;
    private Bitmap profilePicture;

    public SignUpSubmissionBuilder setSignUpForm(SignUpForm signUpForm){
       this.signUpForm = signUpForm;
       return this;
    }

    public SignUpSubmissionBuilder setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    public SignUpSubmission createSignUpSubmission() {
        return SignUpSubmission.newInstance(signUpForm,profilePicture);
    }
}