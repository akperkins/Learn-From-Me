package com.overnightApps.myapplication.app.service;

import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.LetterDao;

/**
 * Created by andre on 4/18/14.
 */
public class PublicLetterSender extends LetterSender {

    public PublicLetterSender(Letter originalLetter, User currentUser, LetterDao letterDao) {
        super(originalLetter, currentUser, letterDao);
    }

    @Override
    protected String getUserSignature() {
        return currentUser.getPublicSignature();
    }

    @Override
    protected boolean isSignedPublicly() {
        return true;
    }
}
