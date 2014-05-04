package com.overnightApps.myapplication.app.ui.optionsMenus.menuOptionFactories;

import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuItem;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptions;
import com.overnightApps.myapplication.app.ui.FragmentIds;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 *
 */
public class HomeMenuOptionsFactory extends MenuOptionsFactory {
    public static final int FRIEND_MENU_OPTION_ID = FragmentIds.FRIEND.getId();
    public static final int LEARN_MENU_OPTION_ID = FragmentIds.LEARN.getId();
    public static final int USER_MENU_OPTION_ID = FragmentIds.USER.getId();
    public static final int SETTINGS_MENU_OPTION_ID = FragmentIds.SETTINGS.getId();
    public static final int RECOMMENDATIONS_MENU_OPTION_ID = FragmentIds.RECEIVE_RECOMMENDATIONS.getId();
    public static final int SIGN_IN_MENU_OPTION_ID = 10000;
    public static final int LOGIN_MENU_OPTION_ID = FragmentIds.LOG_IN.getId();
    public static final int FRIEND_REQUESTS_MENU_OPTION_ID = FragmentIds.FRIEND_REQUESTS.getId();
    public static final String SETTINGS_WINDOW_LABEL = "Settings";
    public static final String SIGN_IN_WINDOW_LABEL = "Sign In";

    public HomeMenuOptionsFactory(User user) {
        super(user);
    }

    @Override
    public MenuOptions newInstance() {
        builder.addItem(new MenuItem(HomeMenuOptionsFactory.LEARN_MENU_OPTION_ID, "Learn"));

        if (isUserSignedIn()) {
            builder.addItem(new MenuItem(HomeMenuOptionsFactory.USER_MENU_OPTION_ID, user.getFullName()))
                    .addItem(new MenuItem(HomeMenuOptionsFactory.RECOMMENDATIONS_MENU_OPTION_ID, "Recommendations"))
                    .addItem(new MenuItem(HomeMenuOptionsFactory.FRIEND_MENU_OPTION_ID, "Friends"))
                    .addItem(new MenuItem(HomeMenuOptionsFactory.FRIEND_REQUESTS_MENU_OPTION_ID, "Friend Request"))
                    .addItem(new MenuItem(HomeMenuOptionsFactory.SETTINGS_MENU_OPTION_ID, SETTINGS_WINDOW_LABEL));
        } else {
            builder.addItem(new MenuItem(HomeMenuOptionsFactory.LOGIN_MENU_OPTION_ID, SIGN_IN_WINDOW_LABEL));
        }
        return builder.createMenuOptions();
    }
}