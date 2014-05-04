package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.ui.optionsMenu;

import com.overnightApps.myapplication.app.ui.optionsMenus.MenuItem;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptions;
import com.overnightApps.myapplication.app.ui.optionsMenus.menuOptionFactories.HomeMenuOptionsFactory;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created by andre on 3/14/14.
 */
public class HomeMenuOptionFactoryTest extends MenuOptionFactoryTest {
    public void test_newInstance_UserIsLoggedIn_correct() {
        ArrayList<MenuItem> BASELINE_LIST = new ArrayList<>();
        BASELINE_LIST.add(new MenuItem(HomeMenuOptionsFactory.LEARN_MENU_OPTION_ID, "Learn"));
        BASELINE_LIST.add(new MenuItem(HomeMenuOptionsFactory.USER_MENU_OPTION_ID, USERNAME));
        BASELINE_LIST.add(new MenuItem(HomeMenuOptionsFactory.RECOMMENDATIONS_MENU_OPTION_ID, "Recommendations"));
        BASELINE_LIST.add(new MenuItem(HomeMenuOptionsFactory.FRIEND_MENU_OPTION_ID, "Friends"));
        BASELINE_LIST.add(new MenuItem(HomeMenuOptionsFactory.FRIEND_REQUESTS_MENU_OPTION_ID, "Friend Request"));
        BASELINE_LIST.add(new MenuItem(HomeMenuOptionsFactory.SETTINGS_MENU_OPTION_ID, "Settings"));

        MenuOptions homeMenuOptions = new HomeMenuOptionsFactory(getStubUser()).newInstance();
        ArrayList<MenuItem> actualList = (ArrayList<MenuItem>) homeMenuOptions.getItems();

        Assert.assertEquals(BASELINE_LIST, actualList);
    }

    public void test_newInstance_UserIsNotLoggedIn_correct() {
        ArrayList<MenuItem> BASELINE_LIST = new ArrayList<>();
        BASELINE_LIST.add(new MenuItem(HomeMenuOptionsFactory.LEARN_MENU_OPTION_ID, "Learn"));
        BASELINE_LIST.add(new MenuItem(HomeMenuOptionsFactory.LOGIN_MENU_OPTION_ID, "Sign In"));

        MenuOptions homeMenuOptions = new HomeMenuOptionsFactory(null).newInstance();
        ArrayList<MenuItem> actualList = (ArrayList<MenuItem>) homeMenuOptions.getItems();

        Assert.assertEquals(BASELINE_LIST, actualList);
    }
}