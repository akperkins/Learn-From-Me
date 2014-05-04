package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.ui.optionsMenu;

import com.overnightApps.myapplication.app.ui.optionsMenus.MenuItem;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptions;
import com.overnightApps.myapplication.app.ui.optionsMenus.menuOptionFactories.LetterMenuOptionsFactory;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created by andre on 3/14/14.
 */
public class LetterMenuOptionsFactoryTest extends MenuOptionFactoryTest {
    public void test_newInstance_UserIsLoggedIn_correct() {
        ArrayList<MenuItem> BASELINE_LIST = new ArrayList<>();
        BASELINE_LIST.add(new MenuItem(LetterMenuOptionsFactory.POPULAR_MENU_OPTION_ID, "Popular"));
        BASELINE_LIST.add(new MenuItem(LetterMenuOptionsFactory.NEWEST_MENU_OPTION_ID, "Newest"));

        MenuOptions letterMenuOptions = new LetterMenuOptionsFactory(getStubUser()).newInstance();
        ArrayList<MenuItem> actualList = (ArrayList<MenuItem>) letterMenuOptions.getItems();

        Assert.assertEquals(BASELINE_LIST, actualList);
    }

    public void test_newInstance_UserIsNotLoggedIn_correct() {
        ArrayList<MenuItem> BASELINE_LIST = new ArrayList<>();
        BASELINE_LIST.add(new MenuItem(LetterMenuOptionsFactory.POPULAR_MENU_OPTION_ID, "Popular"));
        BASELINE_LIST.add(new MenuItem(LetterMenuOptionsFactory.NEWEST_MENU_OPTION_ID, "Newest"));

        MenuOptions letterMenuOptions = new LetterMenuOptionsFactory(null).newInstance();
        ArrayList<MenuItem> actualList = (ArrayList<MenuItem>) letterMenuOptions.getItems();

        Assert.assertEquals(BASELINE_LIST, actualList);
    }
}
