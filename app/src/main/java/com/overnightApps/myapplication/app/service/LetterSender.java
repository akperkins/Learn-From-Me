package com.overnightApps.myapplication.app.service;

import com.overnightApps.myapplication.app.core.Comment;
import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.LetterDao;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by andre on 4/18/14.
 */
public abstract class LetterSender {
    private final Letter originalLetter;
    final User currentUser;
    private final LetterDao letterDao;

    public static LetterSender newPublicInstance(Letter originalLetter, User currentUser){
             return new PublicLetterSender(originalLetter,currentUser,LetterDao.instance());
    }

    public static LetterSender newPrivateInstance(Letter originalLetter, User currentUser){
        return new PrivateLetterSender(originalLetter,currentUser,LetterDao.instance());
    }

    protected LetterSender(Letter originalLetter, User currentUser, LetterDao letterDao) {
        this.originalLetter = originalLetter;
        this.currentUser = currentUser;
        this.letterDao = letterDao;
    }

    public void sendLetter(String recipients, String message) {
        String signatureString = getUserSignature();
        Letter letter = new Letter(recipients, message, signatureString,
                currentUser, new Date().getTime(), new ArrayList<Comment>(), originalLetter,
                isSignedPublicly());
        letterDao.save(letter);
    }

    protected abstract boolean isSignedPublicly();

    protected abstract String getUserSignature();
}