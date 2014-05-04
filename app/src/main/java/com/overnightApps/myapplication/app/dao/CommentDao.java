package com.overnightApps.myapplication.app.dao;

import com.overnightApps.myapplication.app.core.Comment;
import com.overnightApps.myapplication.app.core.User;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 4/7/14.
 */
public class CommentDao extends Dao<Comment, ParseObject, ParseQuery<ParseObject>> implements Serializable {
    private static final String CLASS_NAME = "LetterComment";
    private static final String AUTHOR = "author";
    private static final String LETTER = "letter";
    private static final String CREATION_TIME = "creationTime";
    private static final String MESSAGE = "message";

    private final ParseObject parseLetter;

    public CommentDao(ParseObject parseLetter) {
        this.parseLetter = parseLetter;
    }

    public List<Comment> getComments(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CLASS_NAME);
        query.whereEqualTo(LETTER, parseLetter);
        List<ParseObject> results = null;
        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<Comment> foundCommentList = new ArrayList<>();
        if(results == null){
            return foundCommentList;
        }else{
             for(ParseObject found:results ){
                foundCommentList.add(convertToDomainObject(found));
            }
            return foundCommentList;
        }
    }

    @Override
    public Comment convertToDomainObject(ParseObject parseObject) {
        User user = UserDao.instance().convertToDomainObject(parseObject.getParseUser(AUTHOR));
        String message = parseObject.getString(MESSAGE);
        long creationTime = parseObject.getLong(CREATION_TIME);
        return new Comment(message,user, creationTime);
    }

    public void saveList(List<Comment> commentList){
        for(Comment comment: commentList){
            save(comment);
        }
    }

    @Override
    protected ParseObject getNewDataClassInstance() {
        return new ParseObject(CLASS_NAME);
    }

    @Override
    protected ParseQuery getNewQueryInstance() {
        return new ParseQuery(CLASS_NAME);
    }

    protected ParseQuery getNewUniqueResultQueryInstance(Comment comment ) {
        ParseQuery query = getNewQueryInstance();
        ParseUser parseUser1 = UserDao.instance().find(comment.getAuthor());
        query.whereEqualTo(AUTHOR, parseUser1);
        query.whereEqualTo(LETTER, parseLetter);
        query.whereEqualTo(CREATION_TIME,comment.getCreationTime());
        return query;
    }

    protected void saveDataClassFromDomain(ParseObject parseObject, Comment comment) {
        parseObject.put(AUTHOR, UserDao.instance().find(comment.getAuthor()));
        parseObject.put(LETTER, parseLetter);
        parseObject.put(MESSAGE, comment.getMessage());
        parseObject.put(CREATION_TIME, comment.getCreationTime());
    }
}