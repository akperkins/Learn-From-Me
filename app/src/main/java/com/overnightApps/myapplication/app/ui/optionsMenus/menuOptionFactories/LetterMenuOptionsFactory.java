package com.overnightApps.myapplication.app.ui.optionsMenus.menuOptionFactories;

import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuItem;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptions;

/**
 * Created by andre on 3/14/14.
 */
public class LetterMenuOptionsFactory extends MenuOptionsFactory {

    public static final int POPULAR_MENU_OPTION_ID = 0;
    public static final int NEWEST_MENU_OPTION_ID = 1;

    public LetterMenuOptionsFactory(User user) {
        super(user);
    }

    @Override
    public MenuOptions newInstance() {
        builder.addItem(new MenuItem(POPULAR_MENU_OPTION_ID, "Popular"))
                .addItem(new MenuItem(NEWEST_MENU_OPTION_ID, "Newest"));

        return builder.createMenuOptions();
    }
}
