package com.overnightApps.myapplication.app.ui.homeFragments.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.LetterVote;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.core.helper.LetterWithUserVote;
import com.overnightApps.myapplication.app.dao.LetterDao;
import com.overnightApps.myapplication.app.dao.LetterVoteDao;
import com.overnightApps.myapplication.app.service.UserSession;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnLetterDetailFragmentListener;
import com.parse.ParseObject;

import junit.framework.Assert;

/**
* Created by andre on 4/24/14.
*/
public class LoadDetailFragmentTask extends AsyncTask<ParseObject,Void,LetterWithUserVote> {
    private Activity activity;
    private UserSession userSession;

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.setProgressBarIndeterminateVisibility(true);
     }

    @Override
    protected LetterWithUserVote doInBackground(ParseObject... params) {
        Assert.assertNotNull(userSession);
        Assert.assertNotNull(activity);
        ParseObject parseLetter = params[0];
        Letter letter = LetterDao.instance().convertToDomainObject(parseLetter);
        final User currentUser = userSession.getCurrentUser();
        LetterVote letterVote;
        if(currentUser != null) {
            letterVote = LetterVoteDao.instance().get(currentUser, letter);
          }else{
            letterVote = null;//TODO - refactor application so that this is not passing null!
        }
        return  new LetterWithUserVote(letter,letterVote);
    }

    @Override
    protected void onPostExecute(LetterWithUserVote letterWithUserVote) {
        super.onPostExecute(letterWithUserVote);
        ((OnLetterDetailFragmentListener)activity).letterClicked(letterWithUserVote);
        activity.setProgressBarIndeterminateVisibility(false);
    }
}