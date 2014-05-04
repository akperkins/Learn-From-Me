package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.core;

import com.overnightApps.myapplication.app.ui.optionsMenus.MenuItem;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptions;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptionsBuilder;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by andre on 3/14/14.
 */
public class MenuOptionsBuilderTest extends TestCase {
    private MenuOptionsBuilder menuOptionsBuilder;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        menuOptionsBuilder = new MenuOptionsBuilder(new ArrayList<MenuItem>(), new HashMap<Integer, MenuItem>());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        menuOptionsBuilder = null;
    }

    public void test_addItem_AddMultipleIdsThrowException_assertFail() throws Exception {
        final int MENU_OPTION_ID = 1;

        boolean isExceptionThrown = false;

        try {
            menuOptionsBuilder.addItem(new MenuItem(MENU_OPTION_ID, ""));
            menuOptionsBuilder.addItem(new MenuItem(MENU_OPTION_ID, ""));
        } catch (AssertionError assertionError) {
            isExceptionThrown = true;
        }
        Assert.assertTrue(isExceptionThrown);
    }

    public void test_createHomeMenuOptions_testEmptyBuild_emptyMenuOption() throws Exception {
        final MenuOptions BASELINE_MENU_OPTION = new MenuOptions(new ArrayList<MenuItem>(), new HashMap<Integer, MenuItem>());
        MenuOptions actualMenuOptions = menuOptionsBuilder.createMenuOptions();
        Assert.assertEquals(BASELINE_MENU_OPTION, actualMenuOptions);
    }

}

