package com.overnightApps.myapplication.app.util;

import junit.framework.Assert;

/**
 * Created by andre on 4/17/14.
 */
public class MyAssert extends Assert {
    public static void assertShouldNotReachHere(String message){
        String MESSAGE = "Error! should not have reached here. Bug in code. ";
        throw new AssertionError(MESSAGE+message);
    }

    public static void assertShouldNotReachHere(){
        assertShouldNotReachHere("");
    }
}