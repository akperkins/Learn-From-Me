package com.overnightApps.myapplication.app.ui.homeFragments.UserLogInStateDependentFragments;

import android.os.Bundle;

import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.service.UserSession;

/**
 * Created by andre on 4/24/14.
 */
public class LetterDetailFragmentUserNotLoggedIn extends LetterDetailFragment {
    public static LetterDetailFragment newInstance(Letter letter, UserSession userSession) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_LETTER, letter);
        arguments.putSerializable(USER_SERVICE_ARG, userSession);
        LetterDetailFragment letterDetailFragment = new LetterDetailFragmentUserNotLoggedIn();
        letterDetailFragment.setArguments(arguments);
        return letterDetailFragment;
    }
}