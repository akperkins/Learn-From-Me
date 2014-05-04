package com.overnightApps.myapplication.app.dao.exceptions;

/**
 * Created by andre on 5/1/14.
 */
public class UnableToSignUpException extends Exception {
    public UnableToSignUpException() {
    }

    public UnableToSignUpException(String detailMessage) {
        super(detailMessage);
    }

    public UnableToSignUpException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnableToSignUpException(Throwable throwable) {
        super(throwable);
    }
}
