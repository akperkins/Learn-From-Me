package com.overnightApps.myapplication.app.ui.homeFragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.dao.FriendshipRequestDao;
import com.overnightApps.myapplication.app.dao.UserDao;
import com.overnightApps.myapplication.app.service.UserSession;
import com.overnightApps.myapplication.app.ui.FragmentIds;
import com.overnightApps.myapplication.app.ui.homeFragments.absFragments.HomeFragment;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnUserFragmentListener;
import com.overnightApps.myapplication.app.util.BitmapUtil;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import junit.framework.Assert;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * interface
 * to handle interaction events.
 * Use the {@link FriendRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FriendRequestsFragment extends HomeFragment {
    public static final String USER = "user";
    private OnUserFragmentListener onUserFragmentListener;

    @InjectView(R.id.lv_friendRequests)
    ListView lv_friendRequests;
    ParseFriendRequestQueryAdapter adapter;

    UserSession userSession;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *.
     * @return A new instance of fragment FriendRequestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendRequestsFragment newInstance(UserSession userSession) {
        FriendRequestsFragment fragment = new FriendRequestsFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, userSession);
        fragment.setArguments(args);
        return fragment;
    }
    public FriendRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assert.assertNotNull(getArguments());
        userSession = (UserSession) getArguments().getSerializable(USER);
        Assert.assertNotNull(userSession);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter =
                new ParseFriendRequestQueryAdapter(getActivity(), new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        // Here we can configure a ParseQuery to our heart's desire.
                        ParseQuery query = new ParseQuery(FriendshipRequestDao.CLASS_NAME);
                        query.whereEqualTo(FriendshipRequestDao.IS_RESPONDED, false);
                        query.whereEqualTo(FriendshipRequestDao.TO, ParseUser.getCurrentUser());
                        return query;
                    }
                });
        // adapter.setTextKey("Message");
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
        swingBottomInAnimationAdapter.setInitialDelayMillis(300);
        swingBottomInAnimationAdapter.setAbsListView(lv_friendRequests);

        lv_friendRequests.setAdapter(swingBottomInAnimationAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_requests, container, false);
        ButterKnife.inject(this,view);
        lv_friendRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseUser friend = adapter.getItem(position).getParseUser(FriendshipRequestDao.FROM);
                onUserFragmentListener.userButtonClick(UserDao.instance().convertToDomainObject(friend));
            }
        });
        return view;
    }

    @Override
    protected int getFragmentId() {
        return FragmentIds.FRIEND_REQUESTS.getId();
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

    public class ParseFriendRequestQueryAdapter extends ParseQueryAdapter<ParseObject> {
        public ParseFriendRequestQueryAdapter(Context context, Class<? extends ParseObject> clazz) {
            super(context, clazz);
        }

        public ParseFriendRequestQueryAdapter(Context context, String className) {
            super(context, className);
        }

        public ParseFriendRequestQueryAdapter(Context context, Class<? extends ParseObject> clazz, int itemViewResource) {
            super(context, clazz, itemViewResource);
        }

        public ParseFriendRequestQueryAdapter(Context context, String className, int itemViewResource) {
            super(context, className, itemViewResource);
        }

        public ParseFriendRequestQueryAdapter(Context context, QueryFactory<ParseObject> queryFactory) {
            super(context, queryFactory);
        }

        public ParseFriendRequestQueryAdapter(Context context, QueryFactory<ParseObject> queryFactory, int itemViewResource) {
            super(context, queryFactory, itemViewResource);
        }

        public View getItemView(final ParseObject object, View view, ViewGroup parent) {
            if (view == null) {
                view = view.inflate(getContext(), R.layout.row_friend, null);
            }

            TextView tv_friendName = (TextView) view.findViewById(R.id.tv_friendName);
            ParseUser friend = object.getParseUser(FriendshipRequestDao.FROM);

            try {
                friend = friend.fetchIfNeeded();
            } catch (ParseException e) {
                Log.e("tag", "error", e);
                e.printStackTrace();
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
