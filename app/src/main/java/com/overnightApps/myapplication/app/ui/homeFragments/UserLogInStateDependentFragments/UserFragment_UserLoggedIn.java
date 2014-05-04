package com.overnightApps.myapplication.app.ui.homeFragments.UserLogInStateDependentFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.service.FriendshipSession;
import com.overnightApps.myapplication.app.service.UserSession;
import com.overnightApps.myapplication.app.util.AUtil;
import com.overnightApps.myapplication.app.util.Logger;
import com.overnightApps.myapplication.app.util.MyAssert;

import junit.framework.Assert;

/**
 * Created by andre on 4/24/14.
 */
public class UserFragment_UserLoggedIn extends UserFragment {
    private static final String CURRENT_USER = "currentUser";

    FriendshipSession.FriendShipState currentFriendShipState;

    public static UserFragment newInstance(UserSession userSession, User selectedUser) {
        UserFragment fragment = new UserFragment_UserLoggedIn();
        Bundle args = new Bundle();
        args.putSerializable(SELECTED_USER, selectedUser);
        args.putSerializable(USER_SERVICE_ARG, userSession);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ll_actionButtons.setVisibility(View.VISIBLE);
        refreshActionButton();
        return v;
    }

    private void refreshActionButton() {
        if (userSession.getCurrentUser() == null ) {
            return;
        }
        if (userSession.getCurrentUser().equals(selectedUser)) {
            b_actionButton.setOnClickListener(new MyOnClickUpdatePictureListener());
            b_actionButton.setText("Change Picture");
        } else {
            FriendshipSession friendshipSession = FriendshipSession.friendshipServiceInstance(
                    userSession.getCurrentUser(), selectedUser);
            updateCurrentFriendShipState(friendshipSession.getFriendshipState());
            MyOnClickChangeFriendStateListener onClickListener = new MyOnClickChangeFriendStateListener();
            onClickListener.setFriendshipSession(friendshipSession);

            b_actionButton.setOnClickListener(onClickListener);
        }
    }

    private void updateCurrentFriendShipState(FriendshipSession.FriendShipState currentFriendShipState) {
        this.currentFriendShipState = currentFriendShipState; //TODO - change to a publish-subscribe design pattern
        updateButtonDisplay(this.currentFriendShipState);
    }

    private void updateButtonDisplay(FriendshipSession.FriendShipState friendShipState) {
        b_actionButton.setText(friendShipState.getMessageForUser());
    }

    private class MyOnClickChangeFriendStateListener implements View.OnClickListener {
        FriendshipSession friendshipSession;

        public void setFriendshipSession(FriendshipSession friendshipSession) {
            this.friendshipSession = friendshipSession;
        }

        @Override
        public void onClick(View v) {
            Assert.assertNotNull(friendshipSession);
            FriendshipSession.FriendShipState initialFriendShipState = friendshipSession.getFriendshipState();
            if (initialFriendShipState == FriendshipSession.FriendShipState.NONE) {
                friendshipSession.sendFriendRequest();
                updateCurrentFriendShipState(FriendshipSession.FriendShipState.WAITING_FOR_RESPONSE);
                Logger.p(getActivity(), "Friend Request Sent");
            } else if (initialFriendShipState == FriendshipSession.FriendShipState.RECEIVED_REQUEST) {
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                friendshipSession.acceptFriendRequest();
                                updateCurrentFriendShipState(FriendshipSession.FriendShipState.UNTRUSTED_FRIEND);
                                break;
                            case DialogInterface.BUTTON_NEUTRAL:
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                friendshipSession.denyFriendRequest();
                                updateCurrentFriendShipState(FriendshipSession.FriendShipState.NONE);
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Handle Request")
                        .setPositiveButton("Accept", onClickListener)
                        .setNeutralButton("Cancel", onClickListener)
                        .setNegativeButton("Ignore", onClickListener).show();

            } else if (initialFriendShipState == FriendshipSession.FriendShipState.TRUSTED_FRIEND) {
                AUtil.confirmUserSelection(getActivity(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                friendshipSession.unTrustUser();
                                updateCurrentFriendShipState(FriendshipSession.FriendShipState.UNTRUSTED_FRIEND);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                });
            } else if (initialFriendShipState == FriendshipSession.FriendShipState.UNTRUSTED_FRIEND) {
                AUtil.confirmUserSelection(getActivity(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                friendshipSession.trustUser();
                                updateCurrentFriendShipState(FriendshipSession.FriendShipState.TRUSTED_FRIEND);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                });
            } else if (initialFriendShipState == FriendshipSession.FriendShipState.WAITING_FOR_RESPONSE) {
                Toast.makeText(getActivity(), "Waiting for other user's response", Toast.LENGTH_SHORT);
            } else {
                MyAssert.assertShouldNotReachHere();
            }
        }
    }

    private class MyOnClickUpdatePictureListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"),
                    PROFILE_PICTURE_REQUEST_CODE);
        }
    }
}
