package com.overnightApps.myapplication.app.ui.optionsMenus.menuOptionFactories;

import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptions;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptionsBuilder;

/**
 * Created by andre on 3/14/14.
 */
abstract class MenuOptionsFactory {
    final User user;
    final MenuOptionsBuilder builder = MenuOptionsBuilder.newInstance();

    MenuOptionsFactory(User user) {
        this.user = user;
    }

    abstract public MenuOptions newInstance();

    boolean isUserSignedIn() {
        return user != null;
    }
}
