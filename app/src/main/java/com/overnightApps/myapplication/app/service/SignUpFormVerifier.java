package com.overnightApps.myapplication.app.service;

import com.overnightApps.myapplication.app.core.helper.SignUpForm;
import com.overnightApps.myapplication.app.dao.UserDao;

/**
 * Created by andre on 4/25/14.
 */
public class SignUpFormVerifier {
    private SignUpForm signUpForm;
    private UserDao userDao;
    boolean clientSideChecksPassed;

    public static SignUpFormVerifier newInstance(SignUpForm signUpForm) {
        return new SignUpFormVerifier(signUpForm, UserDao.instance());
    }

    public SignUpFormVerifier(SignUpForm signUpForm, UserDao userDao) {
        this.signUpForm = signUpForm;
        this.userDao = userDao;
        clientSideChecksPassed = false;
    }

    public StatusCode performClientSideChecks() {
        if (isAnyFieldsBlank()) {
            return StatusCode.REQUIRED_FIELDS_EMPTY;
        } else if (signUpForm.getPassword().length() <= 5) {
            return StatusCode.PASSWORD_TOO_SHORT;
        } else if (userDao.isEmailAddressUsed(signUpForm.getEmail())) {
            return StatusCode.EMAIL_ALREADY_USED;
        }else if(userDao.isPublicSignatureUsed(signUpForm.getPublicSignature())){
            return StatusCode.PUBLIC_SIGNATURE_USED;
        } else if (userDao.isPrivateSignatureUsed(signUpForm.getPrivateSignature())) {
            return StatusCode.PRIVATE_SIGNATURE_USED;
        } else {
            clientSideChecksPassed = true;
            return StatusCode.PASSED_INITIAL_CHECKS;
        }
    }

    private boolean isAnyFieldsBlank() {
        return signUpForm.getEmail().equals("") || signUpForm.getPassword().equals("") || signUpForm.getPublicSignature().equals("")
                || signUpForm.getPrivateSignature().equals("") || signUpForm.getFullName().equals("");
    }

    public void setDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public SignUpForm getSignUpForm() {
        return signUpForm;
    }

    public enum StatusCode {
        PASSWORD_TOO_SHORT("Password is less than 6 characters"),
        REQUIRED_FIELDS_EMPTY("Completely fill form"),
        EMAIL_ALREADY_USED("Email address already in use"),
        PRIVATE_SIGNATURE_USED("Private signature already in use by another user"),
        PUBLIC_SIGNATURE_USED("Public Signature already in use by another user"),
        PASSED_INITIAL_CHECKS ("Information accepted.");

        private final String message;

        StatusCode(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}