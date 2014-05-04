package com.overnightApps.myapplication.app.dao;

import com.overnightApps.myapplication.app.core.Comment;
import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.User;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.List;

/**
 * Created by andre on 3/21/14.
 */
public class LetterDao extends Dao<Letter, ParseObject, ParseQuery<ParseObject>> implements Serializable{
    public static final String RECIPIENT = "Recipient";
    public static final String MESSAGE = "Message";
    public static final String SIGNATURE = "Signature";
    public static final String CREATOR = "creator";
    public static final String ORIGINAL = "original";
    public static final String CLASS_NAME = "Letter";
    public static final String CREATION_TIME = "creationTime";
    public static final String VOTES = "votes";
    public static final String IS_SIGNED_PUBLICLY = "isSignedPublicly";
    public static final String POPULARITY = "popularity";
    private static LetterDao instance;
    private final UserDao userDao;

    private LetterDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public static LetterDao instance() {
        if (instance == null) {
            instance = new LetterDao(UserDao.instance());
        }
        return instance;
    }

    public Letter convertToDomainObject(ParseObject parseLetter) {
        try {
            parseLetter.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String recipient = parseLetter.getString(RECIPIENT);
        String message = parseLetter.getString(MESSAGE);
        String signature = parseLetter.getString(SIGNATURE);
        User user = userDao.convertToDomainObject((ParseUser) parseLetter.get(CREATOR));
        Long creationTime = parseLetter.getLong(CREATION_TIME);
        List<Comment> comments = new CommentDao(parseLetter).getComments();
        Letter original;
        original = getOriginalLetter(parseLetter);
        int votes = parseLetter.getInt(VOTES);
        boolean isSignedPublicly = parseLetter.getBoolean(IS_SIGNED_PUBLICLY);
        return new Letter(recipient, message, signature, user, creationTime, comments, original,
                isSignedPublicly, votes);
    }

    private Letter getOriginalLetter(ParseObject parseLetter) {
        Letter original;
        if(parseLetter.get(ORIGINAL) == null){
            original = null;
        }else{
            original = convertToDomainObject((ParseObject) parseLetter.get(ORIGINAL));
        }
        return original;
    }

    @Override
    protected ParseObject getNewDataClassInstance() {
        return new ParseObject(CLASS_NAME);
    }

    @Override
    protected void saveDataClassFromDomain(ParseObject parseObject, Letter letter) {
        parseObject.put(RECIPIENT, letter.getRecipient());
        parseObject.put(MESSAGE, letter.getMessage());
        parseObject.put(SIGNATURE, letter.getSignature());
        parseObject.put(CREATOR, userDao.find(letter.getCreator()));
        Letter originalLetter = letter.getOriginal();
        if(originalLetter != null) {
            parseObject.put(ORIGINAL, LetterDao.instance().find(letter.getOriginal()));
        }
        parseObject.put(CREATION_TIME, letter.getCreationTime());
        parseObject.put(VOTES,letter.getVotes());
        parseObject.put(IS_SIGNED_PUBLICLY,letter.isSignedPublicly());
        new CommentDao(parseObject).saveList(letter.getCommentList());
    }

    @Override
    protected ParseQuery getNewUniqueResultQueryInstance(Letter letter) {
        ParseQuery query = getNewQueryInstance();
        query.whereEqualTo(SIGNATURE, letter.getSignature());
        query.whereEqualTo(CREATION_TIME, letter.getCreationTime());
        return query;
    }

    @Override
    protected ParseQuery getNewQueryInstance() {
        return new ParseQuery(CLASS_NAME);
    }
}
