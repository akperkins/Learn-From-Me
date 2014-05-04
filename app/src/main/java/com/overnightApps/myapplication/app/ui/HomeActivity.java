package com.overnightApps.myapplication.app.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.LetterVote;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.core.helper.LetterWithUserVote;
import com.overnightApps.myapplication.app.dao.exceptions.SavedUserIsNotFoundOnBackEndException;
import com.overnightApps.myapplication.app.service.UserSession;
import com.overnightApps.myapplication.app.ui.homeFragments.CommentsFragment;
import com.overnightApps.myapplication.app.ui.homeFragments.CreateLetterFragment;
import com.overnightApps.myapplication.app.ui.homeFragments.FriendRequestsFragment;
import com.overnightApps.myapplication.app.ui.homeFragments.FriendsFragment;
import com.overnightApps.myapplication.app.ui.homeFragments.LearnFragment;
import com.overnightApps.myapplication.app.ui.homeFragments.LogInDialogFragment;
import com.overnightApps.myapplication.app.ui.homeFragments.ReceiveRecommendationsFragment;
import com.overnightApps.myapplication.app.ui.homeFragments.SendRecommendationFragment;
import com.overnightApps.myapplication.app.ui.homeFragments.SettingsFragment;
import com.overnightApps.myapplication.app.ui.homeFragments.UserLogInStateDependentFragments.LetterDetailFragmentUserNotLoggedIn;
import com.overnightApps.myapplication.app.ui.homeFragments.UserLogInStateDependentFragments.LetterDetailFragment_UserLoggedIn;
import com.overnightApps.myapplication.app.ui.homeFragments.UserLogInStateDependentFragments.UserFragment_UserLoggedIn;
import com.overnightApps.myapplication.app.ui.homeFragments.UserLogInStateDependentFragments.UserFragment_UserLoggedOut;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnActiveFragmentListener;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnCreateLetterFragmentListener;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnLetterDetailActionListener;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnLetterDetailFragmentListener;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnLoginFragmentListener;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnSignUpActivityListener;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnUserFragmentListener;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnUserLogInListener;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnUserLogOutListener;
import com.overnightApps.myapplication.app.ui.optionsMenus.menuOptionFactories.HomeMenuOptionsFactory;
import com.overnightApps.myapplication.app.ui.signUp.SignUpActivity;
import com.overnightApps.myapplication.app.ui.slidingMenu.OptionsFragment;
import com.overnightApps.myapplication.app.util.Logger;


/**
 * An activity representing a list of Letters. This activity
 * has different presentations for handset and tablet-size devices.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link com.overnightApps.myapplication.app.ui.homeFragments.LearnFragment} and the item details
 * (if present) is a {@link com.overnightApps.myapplication.app.ui.homeFragments.UserLogInStateDependentFragments.LetterDetailFragment}.`
 * <p/>
 */
public class HomeActivity extends Activity
        implements OptionsFragment.OnHomeMenuClickListener, OnActiveFragmentListener,
        OnCreateLetterFragmentListener, OnLetterDetailFragmentListener
        , OnUserFragmentListener, OnUserLogInListener, OnLoginFragmentListener,
        OnSignUpActivityListener, OnLetterDetailActionListener, OnUserLogOutListener {

    public static final int MAIN_CONTAINER = R.id.letter_list_container;
    private static final String CALLED_REQUEST_WINDOW_FEATURE = "isProgressCircleSetupCodeCalled";
    private static final String CURRENT_MENU_OPTION_ID = "currentMenuOptionId";
    private SlidingMenu menu;
    private UserSession userSession;

    private int currentMenuOptionID;
    private boolean isProgressCircleSetupCodeCalled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpCurrentUser();
        restoreState(savedInstanceState);
        setUpUI();
    }

    private void setUpUI() {
        setUpProgressCircleOnActionBar();
        setContentView(R.layout.activity_letter_list);
        setUpSlidingMenu();
        startFragment(OptionsFragment.newInstance(userSession), R.id.menu_frame, false);
        startFragment(LearnFragment.newInstance(userSession),MAIN_CONTAINER,false );
    }

    private void setUpCurrentUser() {
        userSession = UserSession.newInstance();
        try {
            userSession.restoreSavedUser();
        } catch (SavedUserIsNotFoundOnBackEndException e) {
            Log.e("HomeActivity", "Current User Logged In not found on back-end", e);
            logCurrentUserOut();
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            isProgressCircleSetupCodeCalled = savedInstanceState.getBoolean(CALLED_REQUEST_WINDOW_FEATURE);
            currentMenuOptionID = savedInstanceState.getInt(CURRENT_MENU_OPTION_ID);
         }else{
            currentMenuOptionID = HomeMenuOptionsFactory.LEARN_MENU_OPTION_ID;
            isProgressCircleSetupCodeCalled = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(CALLED_REQUEST_WINDOW_FEATURE, isProgressCircleSetupCodeCalled);
        outState.putInt(CURRENT_MENU_OPTION_ID, currentMenuOptionID);
    }

    private void setUpProgressCircleOnActionBar() {
        if(!isProgressCircleSetupCodeCalled) {
            this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            isProgressCircleSetupCodeCalled = true;
        }
    }

    private void setUpSlidingMenu() {
        menu = new SlidingMenu(this);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.menu_frame);
        menu.showMenu(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
            case android.R.id.home:
                menu.toggle();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (menu.isMenuShowing()) {
            menu.toggle();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void optionSelected(int optionId) {
        menu.toggle();
        if (currentMenuOptionID == optionId || optionId == FragmentIds.LOG_IN.getId()) {
            return;
        }
        clearBackStack();
        startSelectedHomeMenu(optionId);
    }

    private void startSelectedHomeMenu(int optionId) {
        if (optionId == HomeMenuOptionsFactory.LEARN_MENU_OPTION_ID) {
            startFragment(LearnFragment.newInstance(userSession), MAIN_CONTAINER, true);
        } else if (optionId == HomeMenuOptionsFactory.FRIEND_MENU_OPTION_ID) {
            startFragment(FriendsFragment.newInstance(userSession), MAIN_CONTAINER, true);
        } else if (optionId == HomeMenuOptionsFactory.USER_MENU_OPTION_ID) {
                startFragment(UserFragment_UserLoggedIn.newInstance(userSession, userSession.getCurrentUser()),
                        MAIN_CONTAINER, true);
        } else if (optionId == HomeMenuOptionsFactory.SETTINGS_MENU_OPTION_ID) {
            startFragment(SettingsFragment.newInstance(), MAIN_CONTAINER, true);
        } else if (optionId == HomeMenuOptionsFactory.RECOMMENDATIONS_MENU_OPTION_ID) {
            startFragment(ReceiveRecommendationsFragment.newInstance(userSession),MAIN_CONTAINER, true);
        } else if (optionId == HomeMenuOptionsFactory.FRIEND_REQUESTS_MENU_OPTION_ID){
            startFragment(FriendRequestsFragment.newInstance(userSession), MAIN_CONTAINER, true);
        }
    }

    void showDialog(DialogFragment dialogFragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        final String DIALOG_TAG = "dialog";
        Fragment previousDialog = getFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (previousDialog != null) {
            fragmentTransaction.remove(previousDialog);
        }
        fragmentTransaction.addToBackStack(null);
        dialogFragment.show(fragmentTransaction, DIALOG_TAG);
    }

    private void startFragment(Fragment frag, int containerId, boolean addToBackStack){
        startFragment( frag, containerId, addToBackStack, "");
    }

    private void startFragment(Fragment frag, int containerId, boolean addToBackStack,
                               String addToTag) {
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        FragmentManager fragmentManager = getFragmentManager();
        String tag =  frag.getClass().getName();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(containerId,frag, tag);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void setActiveFragment(int currentFragmentId) {
        currentMenuOptionID = currentFragmentId;
    }

    @Override
    public void createLetterButtonClick() {
        if (userSession.isUserLoggedIn()) {
            startFragment(CreateLetterFragment.newInstance(userSession, null), MAIN_CONTAINER, true);
        } else {
            promptUserToLogIn();
        }
    }

    @Override
    public void letterClicked(LetterWithUserVote letterWithUserVote) {
        Letter letter = letterWithUserVote.getLetter();
        LetterVote userVoteOnLetter = letterWithUserVote.getLetterVote();
        if(userSession.isUserLoggedIn()){
            startFragment(LetterDetailFragment_UserLoggedIn.newInstance(letter, userSession,
                    userVoteOnLetter), MAIN_CONTAINER, true, String.valueOf(letter.getCreationTime()));
        }else {
            startFragment(LetterDetailFragmentUserNotLoggedIn.newInstance(letter, userSession),
                    MAIN_CONTAINER, true, String.valueOf(letter.getCreationTime()));
        }
    }

    @Override
    public void userButtonClick(User selectedUser) {
        if(userSession.isUserLoggedIn()) {
            startFragment(UserFragment_UserLoggedIn.newInstance(userSession,
                    selectedUser), HomeActivity.MAIN_CONTAINER, true, selectedUser.getEmail());
        }else{
            startFragment(UserFragment_UserLoggedOut.newInstance(userSession,selectedUser),
                    HomeActivity.MAIN_CONTAINER, true, selectedUser.getEmail());
        }
   }

    @Override
    public void logCurrentUserIn() {
        clearBackStack();
        recreate();
    }

    @Override
    public void logCurrentUserOut() {
        clearBackStack();
        userSession.logCurrentUserOut();
        recreate();
    }

    private void clearBackStack(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.popBackStackImmediate();
    }

    @Override
    public void startLoginDialog() {
        showDialog(LogInDialogFragment.newInstance(userSession));
    }

    @Override
    public void startSignUpProcess() {
        finish();//TODO - develop a better way of handling the SignUpActivity creating HomeActivity than this
        startActivity(new Intent(this, SignUpActivity.class));
    }
    @Override
    public void addComment(Letter letter) {
        startFragment(CommentsFragment.newInstance(userSession,letter), MAIN_CONTAINER, true);
    }

    @Override
    public void createReplyTo(Letter letter) {
        startFragment(CreateLetterFragment.newInstance(userSession,letter), MAIN_CONTAINER,true);
    }

    @Override
    public void sendRecommendation(Letter letter) {
        if (userSession.isUserLoggedIn()) {
            startFragment(SendRecommendationFragment.newInstance(userSession, letter),
                    MAIN_CONTAINER, true);
        } else {
            promptUserToLogIn();
        }
    }

    private void promptUserToLogIn() {
        Logger.p(this, "Must be logged in to do that!");
        startLoginDialog();
    }

    public UserSession getUserSession() {
        return userSession;
    }
}