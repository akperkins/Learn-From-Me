package com.overnightApps.myapplication.app.dao;

import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.LetterVote;
import com.overnightApps.myapplication.app.core.User;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;

/**
 * Created by andre on 3/21/14.
 */
public class LetterVoteDao extends Dao<LetterVote, ParseObject, ParseQuery<ParseObject>> implements Serializable {
    private static final String CLASS_NAME = "LetterVotes";
    private static final String VOTER = "voter";
    private static final String LETTER = "letter";
    private static final String VOTE_VALUE = "voteValue";
    private static LetterVoteDao letterVoteDao;
    private final UserDao userDao;
    private final LetterDao letterDao;

    private LetterVoteDao(UserDao userDao, LetterDao letterDao) {
        this.userDao = userDao;
        this.letterDao = letterDao;
    }

    public static LetterVoteDao instance() {
        if (letterVoteDao == null) {
            letterVoteDao = new LetterVoteDao(UserDao.instance(), LetterDao.instance());
        }
        return letterVoteDao;
    }

    @Override
    public LetterVote convertToDomainObject(ParseObject parseObject) {
        User user = userDao.convertToDomainObject((ParseUser) (parseObject.get(VOTER)));
        Letter letter = letterDao.convertToDomainObject(parseObject.getParseObject(LETTER));
        LetterVote.Vote vote = LetterVote.Vote.getVoteType(parseObject.getInt(VOTE_VALUE));
        return new LetterVote(user, letter, vote);
    }

    @Override
    protected ParseObject getNewDataClassInstance() {
        return new ParseObject(CLASS_NAME);
    }

    @Override
    protected void saveDataClassFromDomain(ParseObject parseObject, LetterVote letterVote) {
        parseObject.put(VOTER,userDao.find(letterVote.getUser()));
        parseObject.put(LETTER, letterDao.find(letterVote.getLetter()));
        parseObject.put(VOTE_VALUE, letterVote.getVoteValue());
    }

    @Override
    protected ParseQuery getNewUniqueResultQueryInstance(LetterVote letterVote) {
        ParseQuery query = getNewQueryInstance();
        query.whereEqualTo(VOTER, userDao.find(letterVote.getUser()));
        query.whereEqualTo(LETTER, letterDao.find(letterVote.getLetter()));
        return query;
    }

    @Override
    protected ParseQuery getNewQueryInstance() {
        return new ParseQuery(CLASS_NAME);
    }

    public LetterVote get(User user, Letter letter) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CLASS_NAME);
        query.whereEqualTo(VOTER, userDao.find(user));
        query.whereEqualTo(LETTER, letterDao.find(letter));
        ParseObject searchParseObject = findSingleObject(query);
        if(searchParseObject == null) {
            return null;
        }
        return convertToDomainObject(searchParseObject);
      }
}
