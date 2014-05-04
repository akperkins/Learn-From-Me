package com.overnightApps.myapplication.app.ui.homeFragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.dao.FriendshipDao;
import com.overnightApps.myapplication.app.dao.UserDao;
import com.overnightApps.myapplication.app.service.UserSession;
import com.overnightApps.myapplication.app.ui.FragmentIds;
import com.overnightApps.myapplication.app.ui.homeFragments.absFragments.FilterListFragment;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnUserFragmentListener;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptions;
import com.overnightApps.myapplication.app.ui.optionsMenus.menuOptionFactories.FriendMenuOptionsFactory;
import com.overnightApps.myapplication.app.util.BitmapUtil;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import junit.framework.Assert;

/**
 * Created by andre on 3/5/14.
 */
public class FriendsFragment extends FilterListFragment {
    private static final String USER_SERVICE_ARG = "userServiceArg";
    private OnUserFragmentListener onUserFragmentListener;
    private ParseFriendQueryAdapter adapter;
    private UserSession userSession;

    public FriendsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assert.assertNotNull(getArguments());
        userSession = (UserSession)getArguments().getSerializable(USER_SERVICE_ARG);
        Assert.assertNotNull(userSession);
    }

    public static FriendsFragment newInstance(UserSession userSession) {
        Bundle arguments = new Bundle();
        FriendsFragment friendsFragment = new FriendsFragment();
        arguments.putSerializable(USER_SERVICE_ARG, userSession);
        friendsFragment.setArguments(arguments);
        return friendsFragment;
    }

    @Override
    public int getFragmentId() {
        return FragmentIds.FRIEND.getId();
    }

    @Override
    public ActionBar.OnNavigationListener getMenuOnNavigationListener() {
        return new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                final boolean isUserTrusted;
                isUserTrusted = itemPosition != FriendMenuOptionsFactory.UNTRUSTED_MENU_OPTION_ID;
                adapter =
                        new ParseFriendQueryAdapter(getActivity(), new ParseQueryAdapter.QueryFactory<ParseObject>() {
                            public ParseQuery<ParseObject> create() {
                                // Here we can configure a ParseQuery to our heart's desire.
                                ParseQuery query = new ParseQuery(FriendshipDao.CLASS_NAME);
                                query.whereEqualTo(FriendshipDao.USER, ParseUser.getCurrentUser());
                                query.whereEqualTo(FriendshipDao.IS_TRUSTED, isUserTrusted);
                                return query;
                            }
                        });
                setSwingAnimation();
                return true;
            }
        };
    }

    @Override
    public MenuOptions getMenuOptions() {
        return new FriendMenuOptionsFactory(userSession.getCurrentUser()).newInstance();
    }

    void setSwingAnimation() {
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
        swingBottomInAnimationAdapter.setInitialDelayMillis(300);
        swingBottomInAnimationAdapter.setAbsListView(getListView());

        setListAdapter(swingBottomInAnimationAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //TODO remove userDao
        ParseUser friend = null;
        friend = adapter.getItem(position).getParseUser(FriendshipDao.FRIEND);
        onUserFragmentListener.userButtonClick(UserDao.instance().convertToDomainObject(friend));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
            onUserFragmentListener = (OnUserFragmentListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onUserFragmentListener = null;
    }

 /*   *//**
     * Created by andre on 3/13/14.
     */
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
            friend = object.getParseUser(FriendshipDao.FRIEND);
            try {
                friend = friend.fetchIfNeeded();
            } catch (ParseException e) {
                Log.e("tag", "error", e);
            }

            String friendName = friend.getString(UserDao.FULL_NAME);
            tv_friendName.setText(friendName);

            final ImageView iv_friendProfilePicture = (ImageView) view.findViewById(R.id.iv_friendProfilePicture);
            ParseFile profilePicture = (ParseFile) friend.get(UserDao.PROFILE_PICTURE);
            profilePicture.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bitmap = BitmapUtil.byteArrayToBitmap(data);
                        iv_friendProfilePicture.setImageBitmap(bitmap);
                           } else {
                        Log.e("FriendsFragment","Error downloading friend fragment",e);
                    }
                }
            });
            return view;
        }
    }
}