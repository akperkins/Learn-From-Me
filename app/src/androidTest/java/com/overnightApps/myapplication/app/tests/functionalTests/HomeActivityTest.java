package com.overnightApps.myapplication.app.tests.functionalTests;

import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;

import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.ui.HomeActivity;
import com.overnightApps.myapplication.app.ui.optionsMenus.menuOptionFactories.HomeMenuOptionsFactory;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.swipeRight;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

/**
 * Created by andre on 2/23/14.
 */
public class HomeActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    private HomeActivity homeActivity;

    public HomeActivityTest() {
        super("com.overnightApps.myapplication.app", HomeActivity.class);
    }

    Fragment fragment;

    protected void setUp() throws Exception {
        super.setUp();
        homeActivity = getActivity();
    }

    public void test_logIn_userEntersCorrectInformation_userIsLoggedInToApplication() throws Exception {
        ensureUserLoggedOut();

        onView(withText(HomeMenuOptionsFactory.SIGN_IN_WINDOW_LABEL)).perform(click());
        onView(withId(R.id.et_email)).perform(typeText("test@test.com"));
        onView(withId(R.id.et_password)).perform(typeText("tester"));
        onView(withText(homeActivity.getString(R.string.logInButtonText))).perform(click());
        onView(withId(HomeActivity.MAIN_CONTAINER)).perform(swipeRight());
        onView(withText("jonathan maskeltov")).check(matches(isDisplayed()));
    }

    public void test_createLetter_userIsNotLoggedIn_theLogInScreenShouldAppearAndStopUserFromCreatingALetter() throws Exception{
        ensureUserLoggedOut();

        onView(withId(HomeActivity.MAIN_CONTAINER)).perform(click());
        onView(withId(R.id.act_createLetter)).perform(click());
        onView(withId(R.layout.log_in_dialog)).check(matches(isDisplayed()));
    }

/*    public void test_ratingLetters_userIsNotLoggedIn_theRateLetterButtonsShouldNotBeVisible(){
        ensureUserLoggedOut();
        TODO - finish this
        onView(withId(R.layout.activity_letter_list)).;
    }*/

    private void logUserOut() {
        onView(withId(R.id.letter_list_container)).perform(swipeRight());
        onView(withText(HomeMenuOptionsFactory.SETTINGS_WINDOW_LABEL)).perform(click());
        onView(withText(homeActivity.getString(R.string.logOutButtonText))).perform(click());
        onView(withText(homeActivity.getString(R.string.cofirmUserMessage))).perform(click());
    }

    private void ensureUserLoggedOut() {
        if(isUserLoggedIn()) {
            logUserOut();
        }
    }

    private boolean isUserLoggedIn(){
        return homeActivity.getUserSession().isUserLoggedIn();
    }

}