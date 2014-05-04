package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.ui.optionsMenu;

import com.overnightApps.myapplication.app.ui.optionsMenus.MenuItem;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptions;
import com.overnightApps.myapplication.app.ui.optionsMenus.menuOptionFactories.FriendMenuOptionsFactory;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created by andre on 3/14/14.
 */
public class FriendMenuOptionFactoryTest extends MenuOptionFactoryTest {
    public void test_newInstance_testUserIsLoggedIn_correct() {
        ArrayList<MenuItem> BASELINE_LIST = new ArrayList<>();
        BASELINE_LIST.add(new MenuItem(FriendMenuOptionsFactory.TRUSTED_MENU_OPTION_ID, "Trusted"));
        BASELINE_LIST.add(new MenuItem(FriendMenuOptionsFactory.UNTRUSTED_MENU_OPTION_ID, "Untrusted"));

        MenuOptions discussionMenuOptions = new FriendMenuOptionsFactory(getStubUser()).newInstance();
        ArrayList<MenuItem> actualList = (ArrayList<MenuItem>) discussionMenuOptions.getItems();

        Assert.assertEquals(BASELINE_LIST, actualList);
    }
}
