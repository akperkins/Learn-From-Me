package com.overnightApps.myapplication.app.ui.homeFragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.UserDao;
import com.overnightApps.myapplication.app.service.RecommendationCreator;
import com.overnightApps.myapplication.app.service.UserSession;
import com.overnightApps.myapplication.app.ui.FragmentIds;
import com.overnightApps.myapplication.app.ui.homeFragments.absFragments.MyListFragment;
import com.overnightApps.myapplication.app.util.BitmapUtil;
import com.overnightApps.myapplication.app.util.Logger;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 */
public class SendRecommendationFragment extends MyListFragment {
    private static final String LETTER_ARG = "LetterARg";
    private static final String USER_SERVICE_ARG = "userServiceArg";

    private ParseFriendQueryAdapter adapter;
    private Letter letterBeingRecommended;
    private UserSession userSession;

    public static SendRecommendationFragment newInstance(UserSession userSession, Letter letter) {
        SendRecommendationFragment fragment = new SendRecommendationFragment();
        Bundle args = new Bundle();
        args.putSerializable(LETTER_ARG,letter);
        args.putSerializable(USER_SERVICE_ARG, userSession);
        fragment.setArguments(args);
        return fragment;
    }

    public SendRecommendationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assert.assertNotNull(getArguments());
        letterBeingRecommended = (Letter) getArguments().get(LETTER_ARG);
        userSession = (UserSession) getArguments().getSerializable(USER_SERVICE_ARG);
        Assert.assertNotNull(letterBeingRecommended);
        Assert.assertNotNull(userSession);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter =
                new ParseFriendQueryAdapter(getActivity(), new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        ParseQuery query = new ParseQuery("Friendship");
                        query.whereEqualTo("user", ParseUser.getCurrentUser());
                        return query;
                    }
                });
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
        swingBottomInAnimationAdapter.setInitialDelayMillis(300);
        swingBottomInAnimationAdapter.setAbsListView(getListView());

        setListAdapter(swingBottomInAnimationAdapter);
    }

    @Override
    public int getFragmentId() {
        return FragmentIds.SEND_RECOMMENDATION.getId();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        User user = UserDao.instance().convertToDomainObject(adapter.getItem(position).getParseUser("friend"));
        ArrayList<User> userArrayList = new ArrayList<>();
        userArrayList.add(user);
        Logger.p(getActivity(), "Recommendation Sent");
        removeThisFragmentFromStack();
        new SendRecommendationToFriendsTask().execute(userArrayList);
    }

    class SendRecommendationToFriendsTask extends AsyncTask<ArrayList<User>,Void,Void>{
        @Override
        protected Void doInBackground(ArrayList<User>... params) {
            RecommendationCreator recommendationCreator = RecommendationCreator.newInstance();
            recommendationCreator.sendToList(userSession.getCurrentUser(), params[0], letterBeingRecommended);
            return null;
        }
    }

    public class ParseFriendQueryAdapter extends ParseQueryAdapter<ParseObject> {
        public ParseFriendQueryAdapter(Context context, Class<? extends ParseObject> clazz) {
            super(context, clazz);
        }

        public ParseFriendQueryAdapter(Context context, String className) {
            super(context, className);
        }

        public ParseFriendQueryAdapter(Context context, Class<? extends ParseObject> clazz, int itemViewResource) {
            super(context, clazz, itemViewResource);
        }

        public ParseFriendQueryAdapter(Context context, String className, int itemViewResource) {
            super(context, className, itemViewResource);
        }

        public ParseFriendQueryAdapter(Context context, QueryFactory<ParseObject> queryFactory) {
            super(context, queryFactory);
        }

        public ParseFriendQueryAdapter(Context context, QueryFactory<ParseObject> queryFactory, int itemViewResource) {
            super(context, queryFactory, itemViewResource);
        }

        public View getItemView(final ParseObject object, View view, ViewGroup parent) {
            if (view == null) {
                view = view.inflate(getContext(), R.layout.row_friend, null);
            }

            TextView tv_friendName = (TextView) view.findViewById(R.id.tv_friendName);
            ParseUser friend = null;
            friend = object.getParseUser("friend");

            try {
                friend = friend.fetchIfNeeded();
            } catch (ParseException e) {
                Log.e("tag", "error", e);
                e.printStackTrace();
            }

            String friendName = friend.getString("fullName");
            tv_friendName.setText(friendName);

            final ImageView iv_friendProfilePicture = (ImageView) view.findViewById(R.id.iv_friendProfilePicture);
            ParseFile profilePicture = (ParseFile) friend.get("profilePicture");
            profilePicture.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bitmap = BitmapUtil.byteArrayToBitmap(data);
                        iv_friendProfilePicture.setImageBitmap(bitmap);
                    } else {
                        Integer.parseInt("Throw exception");
                    }
                }
            });
            return view;
        }
    }
}
