package com.overnightApps.myapplication.app.ui.homeFragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.exceptions.DataClassNotFoundException;
import com.overnightApps.myapplication.app.dao.LetterDao;
import com.overnightApps.myapplication.app.dao.LetterRecommendationDao;
import com.overnightApps.myapplication.app.dao.UserDao;
import com.overnightApps.myapplication.app.service.FriendshipSession;
import com.overnightApps.myapplication.app.service.UserSession;
import com.overnightApps.myapplication.app.ui.FragmentIds;
import com.overnightApps.myapplication.app.ui.homeFragments.absFragments.MyListFragment;
import com.overnightApps.myapplication.app.ui.homeFragments.asyncTasks.LoadDetailFragmentTask;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnLetterDetailFragmentListener;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import junit.framework.Assert;

/**
 * Created by andre on 4/2/14.
 */
public class ReceiveRecommendationsFragment extends MyListFragment {
    private static final String USER_SERVICE_ARG = "User Service Arg";
    private UserSession userSession;
    private OnLetterDetailFragmentListener onLetterDetailFragmentListener;
    private ParseQuery<ParseObject> query;
    private ParseLetterRecommendationsQuery adapter;

    public ReceiveRecommendationsFragment() {
    }

    public static ReceiveRecommendationsFragment newInstance(UserSession userSession){
        Bundle arguments = new Bundle();
        arguments.putSerializable(USER_SERVICE_ARG, userSession);
        ReceiveRecommendationsFragment receiveRecommendationsFragment = new ReceiveRecommendationsFragment();
        receiveRecommendationsFragment.setArguments(arguments);
        return receiveRecommendationsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assert.assertNotNull(getArguments());
        userSession = (UserSession) getArguments().getSerializable(USER_SERVICE_ARG);
        Assert.assertNotNull(userSession);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onLetterDetailFragmentListener = (OnLetterDetailFragmentListener)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onLetterDetailFragmentListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ParseUser parseUser;
        try {
            parseUser = UserDao.instance().find(userSession.getCurrentUser()); //TODO - move to function in userService
        } catch (DataClassNotFoundException dataClassNotFoundException) {
            Log.e("","error while loading recommendations try again later", dataClassNotFoundException);
            dataClassNotFoundException.printStackTrace();
            return;
        }
        loadUserRecommendations(parseUser);
    }

    private void loadUserRecommendations(ParseUser parseUser) {
        query = new ParseQuery(LetterRecommendationDao.CLASS_NAME);
        query.orderByDescending(LetterRecommendationDao.CREATED_AT_TAG);
        query.whereEqualTo(LetterRecommendationDao.TO, parseUser);
        adapter =
                new ParseLetterRecommendationsQuery(getActivity(),
                        new ParseQueryAdapter.QueryFactory<ParseObject>() {
                            public ParseQuery<ParseObject> create() {
                                return query;
                            }
                        });
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
        swingBottomInAnimationAdapter.setInitialDelayMillis(300);
        swingBottomInAnimationAdapter.setAbsListView(getListView());
        setListAdapter(swingBottomInAnimationAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        LoadDetailFragmentTask loadDetailFragmentTask = new LoadDetailFragmentTask();
        loadDetailFragmentTask.setActivity(getActivity());
        loadDetailFragmentTask.setUserSession(userSession);
        loadDetailFragmentTask.execute(adapter.getItem(0));
    }

    @Override
    public int getFragmentId() {
        return FragmentIds.RECEIVE_RECOMMENDATIONS.getId();
    }

    private class ParseLetterRecommendationsQuery extends ParseQueryAdapter<ParseObject> {
        private ParseLetterRecommendationsQuery(Context context, Class clazz) {
            super(context, clazz);
        }

        private ParseLetterRecommendationsQuery(Context context, String className) {
            super(context, className);
        }

        private ParseLetterRecommendationsQuery(Context context, Class clazz, int itemViewResource) {
            super(context, clazz, itemViewResource);
        }

        private ParseLetterRecommendationsQuery(Context context, String className, int itemViewResource) {
            super(context, className, itemViewResource);
        }

        private ParseLetterRecommendationsQuery(Context context, ParseQueryAdapter.QueryFactory queryFactory) {
            super(context, queryFactory);
        }

        private ParseLetterRecommendationsQuery(Context context, ParseQueryAdapter.QueryFactory queryFactory, int itemViewResource) {
            super(context, queryFactory, itemViewResource);
        }

        public View getItemView(ParseObject recommendation, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.letter_recommendations, null);
            }
            ParseObject letter = recommendation.getParseObject(LetterRecommendationDao.LETTER);
            try {
                letter = letter.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            TextView recipient = (TextView) v.findViewById(R.id.tv_recipient);
            recipient.setText("Dear, " + letter.getString(LetterDao.RECIPIENT));
            TextView message = (TextView) v.findViewById(R.id.tv_message);
            message.setText(letter.getString(LetterDao.MESSAGE));
            TextView signature = (TextView) v.findViewById(R.id.tv_signature);
            signature.setText(letter.getString(LetterDao.SIGNATURE));

            //referer
            new LoadFriendShipAsyncTask(v).execute(recommendation);
               return v;
        }
    }

    class LoadFriendShipAsyncTask extends AsyncTask<ParseObject,Void,String>{
        final View listItem;

        LoadFriendShipAsyncTask(View listItem) {
            this.listItem = listItem;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected String doInBackground(ParseObject... params) {
            ParseObject recommendation = params[0];
            String refererSignature = "";
            User referer = UserDao.instance().convertToDomainObject(recommendation.getParseUser(LetterRecommendationDao.FROM));
            FriendshipSession friendshipSession = FriendshipSession.friendshipServiceInstance(
                    userSession.getCurrentUser(), referer);
            if(friendshipSession.isFriendTrusted()){
                refererSignature += "Trusted User";
            }else{
                refererSignature += referer.getFullName();
            }
            return refererSignature;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            getActivity().setProgressBarIndeterminateVisibility(false);
            TextView tv_referer = (TextView) listItem.findViewById(R.id.tv_referer);
            tv_referer.setText("Referred by: " + s);
        }
    }
}