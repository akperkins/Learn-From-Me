package com.overnightApps.myapplication.app.ui.optionsMenus.menuOptionFactories;

import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuItem;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptions;

import junit.framework.Assert;

/**
 * Created by andre on 3/14/14.
 */
public class FriendMenuOptionsFactory extends MenuOptionsFactory {
    public static final int TRUSTED_MENU_OPTION_ID = 0;
    public static final int UNTRUSTED_MENU_OPTION_ID = 1;

    public FriendMenuOptionsFactory(User user) {
        super(user);
    }

    @Override
    public MenuOptions newInstance() {
        Assert.assertTrue(isUserSignedIn());
        builder.addItem(new MenuItem(TRUSTED_MENU_OPTION_ID, "Trusted"))
                .addItem(new MenuItem(UNTRUSTED_MENU_OPTION_ID, "Untrusted"));
        return builder.createMenuOptions();
    }
}
