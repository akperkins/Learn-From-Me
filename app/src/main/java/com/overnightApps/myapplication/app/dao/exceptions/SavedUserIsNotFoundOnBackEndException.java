package com.overnightApps.myapplication.app.dao.exceptions;

/**
 * Created by andre on 4/20/14.
 */
public class SavedUserIsNotFoundOnBackEndException extends Exception {
    public SavedUserIsNotFoundOnBackEndException() {
    }

    public SavedUserIsNotFoundOnBackEndException(String detailMessage) {
        super(detailMessage);
    }

    public SavedUserIsNotFoundOnBackEndException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SavedUserIsNotFoundOnBackEndException(Throwable throwable) {
        super(throwable);
    }
}
