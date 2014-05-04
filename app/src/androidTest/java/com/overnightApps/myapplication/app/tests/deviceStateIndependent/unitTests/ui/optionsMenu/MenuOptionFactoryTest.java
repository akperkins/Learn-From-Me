package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.ui.optionsMenu;

import com.overnightApps.myapplication.app.core.User;

import junit.framework.TestCase;

/**
 * Created by andre on 4/2/14.
 */
public class MenuOptionFactoryTest extends TestCase {
    final String USERNAME = "hamilton";

    User getStubUser(){
        return new User("",USERNAME,"",0,"","");
    }
}
