package com.overnightApps.myapplication.app.dao;

import com.overnightApps.myapplication.app.core.FriendShipRequest;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.exceptions.DataClassNotFoundException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;

/**
 * Created by andre on 3/25/14.
 */
public class FriendshipRequestDao extends Dao<FriendShipRequest, ParseObject, ParseQuery<ParseObject>> implements Serializable {
    public static final String CLASS_NAME = "FriendRequest";
    public static final String TO = "to";
    public static final String FROM = "from";
    public static final String IS_ACCEPTED = "isAccepted";
    public static final String IS_RESPONDED = "isResponded";
    private static FriendshipRequestDao instance;
    private final UserDao userDao;

    private FriendshipRequestDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public static FriendshipRequestDao instance() {
        if (instance == null) {
            instance = new FriendshipRequestDao(UserDao.instance());
        }
        return instance;
    }

    public FriendShipRequest get(User firstUser, User secondUser) {
        ParseUser parseFirstUser = userDao.find(firstUser);
        ParseUser parseSecondUser = userDao.find(secondUser);
            ParseObject result = getBySender(parseFirstUser, parseSecondUser);
        if (result != null) {
            return convertToDomainObject(result);
        }
        result = getBySender(parseSecondUser, parseFirstUser);
        if (result != null) {
            return convertToDomainObject(result);
        }
        return null;
    }

    private ParseObject getBySender(ParseUser to, ParseUser from) throws DataClassNotFoundException {
        ParseQuery query = new ParseQuery(CLASS_NAME);
        query.whereEqualTo(TO, to);
        query.whereEqualTo(FROM, from);
        return findSingleObject(query);
    }

    @Override
    public FriendShipRequest convertToDomainObject(ParseObject parseObject) {
        User to = userDao.convertToDomainObject(parseObject.getParseUser(TO));
        User from = userDao.convertToDomainObject(parseObject.getParseUser(FROM));
        boolean isAccepted = parseObject.getBoolean(IS_ACCEPTED);
        boolean isResponded = parseObject.getBoolean(IS_RESPONDED);
        return new FriendShipRequest(to, from, isAccepted, isResponded);
    }

    @Override
    protected ParseObject getNewDataClassInstance() {
        return new ParseObject(CLASS_NAME);
    }

    @Override
    protected void saveDataClassFromDomain(ParseObject parseObject, FriendShipRequest friendShipRequest) {
        parseObject.put(TO,userDao.find(friendShipRequest.getTo()));
        parseObject.put(FROM,userDao.find(friendShipRequest.getFrom()));
        parseObject.put(IS_ACCEPTED,friendShipRequest.isAccepted());
        parseObject.put(IS_RESPONDED,friendShipRequest.isResponded());
    }

    @Override
    protected ParseQuery getNewUniqueResultQueryInstance(FriendShipRequest friendShipRequest ) {
        ParseQuery query = getNewQueryInstance();
        query.whereEqualTo(TO, userDao.find(friendShipRequest.getTo()));
        query.whereEqualTo(FROM, userDao.find(friendShipRequest.getFrom()));
        return query;
    }

    @Override
    protected ParseQuery getNewQueryInstance() {
        return new ParseQuery(CLASS_NAME);
    }
}
