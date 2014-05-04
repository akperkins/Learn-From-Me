package com.overnightApps.myapplication.app.dao;

import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.LetterRecommendation;
import com.overnightApps.myapplication.app.core.User;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;

/**
 * Created by andre on 4/11/14.
 */
public class LetterRecommendationDao extends Dao<LetterRecommendation, ParseObject, ParseQuery<ParseObject>> implements Serializable {
    public static final String TO = "to";
    public static final String FROM = "from";
    public static final String LETTER = "letter";
    public  static final String CLASS_NAME = "LetterRecommendations";

    private final UserDao userDao;
    private final LetterDao letterDao;


    public LetterRecommendationDao(UserDao userDao, LetterDao letterDao) {
        this.userDao = userDao;
        this.letterDao = letterDao;
    }

    @Override
    public LetterRecommendation convertToDomainObject(ParseObject parseObject) {
        User to = userDao.convertToDomainObject(parseObject.getParseUser(TO));
        User from = userDao.convertToDomainObject(parseObject.getParseUser(FROM));
        Letter letter = letterDao.convertToDomainObject(parseObject.getParseObject(LETTER));
        return new LetterRecommendation(to,from,letter);
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
    protected void saveDataClassFromDomain(ParseObject parseObject, LetterRecommendation letterRecommendation) {
        parseObject.put(TO, userDao.find(letterRecommendation.getTo()));
        parseObject.put(FROM, userDao.find(letterRecommendation.getFrom()));
        parseObject.put(LETTER, letterDao.find(letterRecommendation.getLetter()));
    }

    @Override
    protected ParseQuery getNewUniqueResultQueryInstance(LetterRecommendation letterRecommendation) {
        ParseQuery query = getNewQueryInstance();
        query.whereEqualTo(TO, userDao.find(letterRecommendation.getTo()));
        query.whereEqualTo(FROM, userDao.find(letterRecommendation.getFrom()));
        query.whereEqualTo(LETTER, letterDao.find(letterRecommendation.getLetter()));
        return query;
    }

}
