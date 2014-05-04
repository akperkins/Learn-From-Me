package com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces;

import com.overnightApps.myapplication.app.core.Letter;

/**
 * Created by andre on 4/6/14.
 */
public interface OnLetterDetailActionListener {
    public void addComment(Letter letter);

    public void createReplyTo(Letter letter);

    public void sendRecommendation(Letter letter);
}
