package com.overnightApps.myapplication.app.ui.signUp;

import android.graphics.Bitmap;

import com.overnightApps.myapplication.app.service.SignUpFormVerifier;

/**
 * Created by andre on 4/20/14.
 */
interface OnSignUpListener {
    public void provideUserInformation(SignUpFormVerifier signUpFormVerifier);
    public void profilePictureSelected(Bitmap bitmap);
}
