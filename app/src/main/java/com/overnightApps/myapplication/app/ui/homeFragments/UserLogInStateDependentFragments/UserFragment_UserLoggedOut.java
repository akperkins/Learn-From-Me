package com.overnightApps.myapplication.app.ui.homeFragments.UserLogInStateDependentFragments;

import android.os.Bundle;

import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.service.UserSession;

/**
 * Created by andre on 4/24/14.
 */
public class UserFragment_UserLoggedOut extends UserFragment {
    public static UserFragment newInstance(UserSession userSession, User selectedUser) {
        UserFragment fragment = new UserFragment_UserLoggedOut();
        Bundle args = new Bundle();
        args.putSerializable(SELECTED_USER, selectedUser);
        args.putSerializable(USER_SERVICE_ARG, userSession);
        fragment.setArguments(args);
        return fragment;
    }
}
