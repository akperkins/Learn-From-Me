package com.overnightApps.myapplication.app.service;

import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.LetterRecommendation;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.LetterDao;
import com.overnightApps.myapplication.app.dao.LetterRecommendationDao;
import com.overnightApps.myapplication.app.dao.UserDao;

import java.util.List;

/**
 * Created by andre on 4/13/14.
 */
public class RecommendationCreator {
    private final LetterRecommendationDao letterRecommendationDao;

    public RecommendationCreator(LetterRecommendationDao letterRecommendationDao) {
        this.letterRecommendationDao = letterRecommendationDao;
    }

    public static RecommendationCreator newInstance(){
        return new RecommendationCreator(new LetterRecommendationDao(
                UserDao.instance(),LetterDao.instance()));
    }

    public void sendToList(User sender, List<User> userList, Letter letter){
        for(User recipient: userList){
            LetterRecommendation letterRecommendation = new LetterRecommendation(recipient,sender,letter);
            letterRecommendationDao.save(letterRecommendation);
        }
    }
}