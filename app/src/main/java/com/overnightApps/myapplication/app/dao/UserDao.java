package com.overnightApps.myapplication.app.dao;

import android.graphics.Bitmap;
import android.util.Log;

import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.core.helper.SignUpForm;
import com.overnightApps.myapplication.app.dao.exceptions.SavedUserIsNotFoundOnBackEndException;
import com.overnightApps.myapplication.app.dao.exceptions.DataClassNotFoundException;
import com.overnightApps.myapplication.app.dao.exceptions.UnableToSignUpException;
import com.overnightApps.myapplication.app.util.BitmapUtil;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;

/**
 * Created by andre on 3/21/14.
 */
public class UserDao extends Dao<User, ParseUser,ParseQuery<ParseUser>> implements Serializable {
    public static final String FULL_NAME = "fullName";
    public static final String EXPERIENCE = "experience";
    public static final String EMAIL = "email";
    public static final String PRIVATE_SIGNATURE = "privateSignature";
    public static final String PUBLIC_SIGNATURE = "publicSignature";
    public static final String PROFILE_PICTURE = "profilePicture";
    public static final String IS_ACTIVE = "isActive";
    public static final String SIGNATURE = "Signature";
    private static UserDao userDao;

    //TODO - class too big, break it down
    private UserDao() {
    }

    public static UserDao instance() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }

    @Override
    protected ParseUser getNewDataClassInstance() {
        return new ParseUser();
    }

    @Override
    protected void saveDataClassFromDomain(ParseUser parseUser, User user) {
        parseUser.setEmail(user.getEmail());
        parseUser.setUsername(user.getEmail());
        parseUser.put(EXPERIENCE,user.getExperience());
        parseUser.put(FULL_NAME, user.getFullName());
        parseUser.put(PUBLIC_SIGNATURE,user.getPublicSignature());
        parseUser.put(PRIVATE_SIGNATURE,user.getPrivateSignature());
    }

    @Override
    protected ParseQuery getNewUniqueResultQueryInstance(User user) {
        ParseQuery query = getNewQueryInstance();
        query.whereEqualTo(EMAIL, user.getEmail());
        return query;
    }

    protected ParseQuery getNewQueryInstance() {
        return ParseUser.getQuery();
    }

    public User getCurrentUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            return convertToDomainObject(currentUser);
        } else {
            return null;
        }
    }

    public boolean isEmailAddressUsed(String email) {
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo(EMAIL,email);
        boolean isEmailAddressUsed;
        try { //TODO - replace with null object pattern
            findSingleObject(parseQuery);
            isEmailAddressUsed = true;
        } catch (DataClassNotFoundException dataClassNotFoundException) {
            isEmailAddressUsed = false;
        }
        return isEmailAddressUsed;
    }

    public User logIn(String email, String password) {
        ParseUser parseUser = null;
        try {
            parseUser = ParseUser.logIn(email, password);
        } catch (ParseException e) {
            Log.e("Tag", "Parse Error", e);
        }
        if(parseUser != null){
            return convertToDomainObject(parseUser);
        }else{
            return null;
        }
    }

    public boolean signUserUp(SignUpForm signUpForm, Bitmap profilePicture) throws UnableToSignUpException {
        ParseUser user = new ParseUser();
        user.setUsername(signUpForm.getEmail());
        user.setPassword(signUpForm.getPassword());
        user.setEmail(signUpForm.getEmail());
        user.put(FULL_NAME,signUpForm.getFullName());
        user.put(IS_ACTIVE,true);
        user.put(EXPERIENCE,0);
        user.put(PUBLIC_SIGNATURE, signUpForm.getPublicSignature());
        user.put(PRIVATE_SIGNATURE, signUpForm.getPrivateSignature());
        try {
            user.signUp();
            uploadUserPicture(profilePicture);
        } catch (ParseException e) {
            throw new UnableToSignUpException("User unable to sing up even after submitting correcting" +
                    " informtation. Info from parse: " + e.getMessage());
        }
        return true;
    }

    public static Bitmap getUserProfilePicture(ParseUser parseUser) {
        ParseFile profilePicture = parseUser.getParseFile(PROFILE_PICTURE);
        Bitmap bitmap = null;
        try {
            bitmap =  BitmapUtil.byteArrayToBitmap(profilePicture.getData());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void uploadUserPicture(Bitmap bitmap) {
        ParseFile parseFile = convertBitmapToParseFile(bitmap);
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.put(PROFILE_PICTURE, parseFile);
        parseUser.saveInBackground();
    }

    public static ParseFile convertBitmapToParseFile(Bitmap bitmap) {
        byte[] bytes = BitmapUtil.toByteArray(bitmap);
        return new ParseFile(PROFILE_PICTURE, bytes);
    }

    public boolean isPublicSignatureUsed(String publicSignature) {
        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereEqualTo(PUBLIC_SIGNATURE,publicSignature);
        boolean isSignatureUsed;
        try {
            findSingleObject(userParseQuery);
            isSignatureUsed = true;
        } catch (DataClassNotFoundException dataClassNotFoundException) {
            isSignatureUsed = false;
            dataClassNotFoundException.printStackTrace();
        }
        return isSignatureUsed;
    }

    public boolean isPrivateSignatureUsed(String privateSignature){
        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereEqualTo(PRIVATE_SIGNATURE,privateSignature);
        boolean isSignatureUsed;
        try {
            findSingleObject(userParseQuery);
            isSignatureUsed = true;
        } catch (DataClassNotFoundException dataClassNotFoundException) {
            isSignatureUsed = false;
            dataClassNotFoundException.printStackTrace();
        }
        return isSignatureUsed;
    }

    public ParseUser findUserByPublicSignature(String signature) throws DataClassNotFoundException {
        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereEqualTo(PUBLIC_SIGNATURE,signature);
        return findSingleObject(userParseQuery);
    }

    public User convertToDomainObject(ParseUser parseUser) {
        ParseUser parseToCreate = null;
        try {
            parseToCreate = parseUser.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new User(parseToCreate.getEmail(),parseToCreate.getString(FULL_NAME)
                ,parseToCreate.getCreatedAt().toString(), parseToCreate.getInt(EXPERIENCE),
                parseToCreate.getString(PUBLIC_SIGNATURE),parseToCreate.getString(PRIVATE_SIGNATURE));
    }

    public Bitmap getSignaturePicture(String signature) throws DataClassNotFoundException {
        ParseUser user = findUserByPublicSignature(signature);
        if(user != null) {
            return getUserProfilePicture(user);
        }else{
            return null;
        }
    }

    public User searchForUserByEmail(String email) throws SavedUserIsNotFoundOnBackEndException {
        User user = new User(email,"","",0,"","");
        ParseUser searchResult;
        try {
            searchResult = find(user);
        } catch (DataClassNotFoundException dataClassNotFoundException) {
            throw new SavedUserIsNotFoundOnBackEndException("Could not find user on back end, most" +
                    " likely the user has been deleted on back-end");
        }
        return convertToDomainObject(searchResult);
    }

    public User getSavedUser() {
        ParseUser currentDataUserFound = ParseUser.getCurrentUser();
        if(currentDataUserFound == null){
            throw new DataClassNotFoundException("Could not find the current user logged in");
        }
        return convertToDomainObject(currentDataUserFound);
    }

    public void logUserOut(){
        ParseUser.logOut();
    }
}