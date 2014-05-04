package com.overnightApps.myapplication.app.dao;

import com.overnightApps.myapplication.app.core.Friendship;
import com.overnightApps.myapplication.app.core.User;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;

/**
 * Created by andre on 3/25/14.
 */
public class FriendshipDao extends Dao<Friendship, ParseObject, ParseQuery<ParseObject>> implements Serializable {
    public static final String USER = "user";
    public static final String FRIEND = "friend";
    public static final String CLASS_NAME = "Friendship";
    public static final String IS_TRUSTED = "isTrusted";
    private final UserDao userDao;
    private static FriendshipDao instance;

    public static FriendshipDao instance(){
        if(instance == null){
            instance = new FriendshipDao(UserDao.instance());
        }
        return instance;
    }

    private FriendshipDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public ParseObject get(User user1, User user2) {
        ParseQuery query = new ParseQuery(CLASS_NAME);
        ParseUser parseUser1 = userDao.find(user1);
        ParseUser parseUser2 = userDao.find(user2);
        query.whereEqualTo(USER, parseUser1);
        query.whereEqualTo(FRIEND, parseUser2);
        return findSingleObject(query);
    }

    public Friendship getFriendship(User user1, User user2){
        return convertToDomainObject(get(user1, user2));
    }

    @Override
    public Friendship convertToDomainObject(ParseObject parseObject) {
        User user = userDao.convertToDomainObject(parseObject.getParseUser(USER));
        User friend = userDao.convertToDomainObject(parseObject.getParseUser(FRIEND));
        boolean isTrusted = parseObject.getBoolean(IS_TRUSTED);
        return new Friendship(user, friend, isTrusted);
    }

    @Override
    protected ParseObject getNewDataClassInstance() {
        return new ParseObject(CLASS_NAME);
    }

    @Override
    protected ParseQuery getNewQueryInstance() {
        return new ParseQuery(CLASS_NAME);
    }

    @Override
    protected void saveDataClassFromDomain(ParseObject parseObject, Friendship friendship) {
        parseObject.put(USER, userDao.find(friendship.getCurrent()));
        parseObject.put(FRIEND, userDao.find(friendship.getFriend()));
        parseObject.put(IS_TRUSTED, friendship.isTrusted());
    }


    protected ParseQuery getNewUniqueResultQueryInstance(Friendship friendship) {
        ParseQuery query = getNewQueryInstance();
        ParseUser parseUser1 = userDao.find(friendship.getCurrent());
        ParseUser parseUser2 = userDao.find(friendship.getFriend());
        query.whereEqualTo(USER, parseUser1);
        query.whereEqualTo(FRIEND, parseUser2);
        return query;
    }

}
