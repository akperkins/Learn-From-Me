package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.ui.optionsMenu;

import com.overnightApps.myapplication.app.ui.optionsMenus.MenuItem;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptions;
import com.overnightApps.myapplication.app.ui.optionsMenus.menuOptionFactories.RecommendationMenuOptionsFactory;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created by andre on 4/2/14.
 */
public class RecommendationMenuOptionFactoryTest extends MenuOptionFactoryTest {

    public void test_newInstance_UserIsLoggedIn_correct() {
        ArrayList<MenuItem> BASELINE_LIST = new ArrayList<>();
        BASELINE_LIST.add(new MenuItem(RecommendationMenuOptionsFactory.ALL_MENU_OPTION_ID, "ALL"));
        BASELINE_LIST.add(new MenuItem(RecommendationMenuOptionsFactory.TRUSTED_MENU_OPTION_ID, "Trusted"));
        BASELINE_LIST.add(new MenuItem(RecommendationMenuOptionsFactory.UNTRUSTED_MENU_OPTION_ID, "Untrusted"));

        MenuOptions homeMenuOptions = new RecommendationMenuOptionsFactory(getStubUser()).newInstance();
        ArrayList<MenuItem> actualList = (ArrayList<MenuItem>) homeMenuOptions.getItems();

        Assert.assertEquals(BASELINE_LIST, actualList);
    }

}
