package com.overnightApps.myapplication.app.dao.exceptions;

/**
 * Created by andre on 4/30/14.
 */
public class DataClassNotFoundException extends RuntimeException {
    public DataClassNotFoundException() {
    }

    public DataClassNotFoundException(String detailMessage) {
        super(detailMessage);
    }

    public DataClassNotFoundException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DataClassNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
