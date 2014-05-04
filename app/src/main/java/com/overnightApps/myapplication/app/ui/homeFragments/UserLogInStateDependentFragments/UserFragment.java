package com.overnightApps.myapplication.app.ui.homeFragments.UserLogInStateDependentFragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.UserDao;
import com.overnightApps.myapplication.app.service.UserSession;
import com.overnightApps.myapplication.app.ui.FragmentIds;
import com.overnightApps.myapplication.app.ui.homeFragments.absFragments.HomeFragment;
import com.overnightApps.myapplication.app.util.BitmapUtil;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import junit.framework.Assert;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link UserFragment#} factory method to
 * create an instance of this fragment.
 */
public abstract class UserFragment extends HomeFragment {
    protected static final int PROFILE_PICTURE_REQUEST_CODE = 1;
    protected static final String SELECTED_USER = "ParseUserID";
    protected static final String USER_SERVICE_ARG = "userServiceArg";
    @InjectView(R.id.tv_reputation)
    TextView tv_experience;
    @InjectView(R.id.iv_profilePicture)
    ImageView iv_profilePicture;
    @InjectView(R.id.tv_username)
    TextView tv_username;
    @InjectView(R.id.lv_letters)
    ListView lv_letters;
    @InjectView(R.id.b_actionButton)
    Button b_actionButton;
    @InjectView(R.id.ll_actionsButtons)
    LinearLayout ll_actionButtons;

    protected User selectedUser;

    protected UserSession userSession;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assert.assertTrue(getArguments() != null);
        selectedUser = (User) getArguments().getSerializable(SELECTED_USER);
        Assert.assertNotNull(selectedUser);

        userSession = (UserSession) getArguments().getSerializable(USER_SERVICE_ARG);
        Assert.assertNotNull(userSession);
    }

    @Override
    public int getFragmentId() {
        return FragmentIds.USER.getId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.inject(this, v);
        tv_experience.setText("Experience: " + selectedUser.getExperience());
        tv_username.setText("name: " + selectedUser.getFullName());
        new DownloadUserPicture().execute();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        ParseUserLetterQueryAdapter adapter =
                new ParseUserLetterQueryAdapter(getActivity(), new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        ParseQuery query = new ParseQuery("Letter");
                        query.whereEqualTo("creator", UserDao.instance().find(selectedUser));
                        return query;
                    }
                });
        lv_letters.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PROFILE_PICTURE_REQUEST_CODE:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri photoUri = data.getData();
                    if (photoUri != null) {
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(
                                    getActivity().getContentResolver(), photoUri);
                            bitmap = BitmapUtil.scaleBitmap(bitmap);
                        } catch (IOException e) {
                            Log.e("TAG", "error while obtaining image", e);
                        }
                        Assert.assertNotNull(bitmap);
                        iv_profilePicture.setImageBitmap(bitmap);
                        UserDao.instance().uploadUserPicture(bitmap);
                    }
                }
        }
    }

    /**
     * Created by andre on 2/26/14.
     */
    public static class ParseUserLetterQueryAdapter extends ParseQueryAdapter<ParseObject> {
        public ParseUserLetterQueryAdapter(Context context, Class clazz) {
            super(context, clazz);
        }

        public ParseUserLetterQueryAdapter(Context context, String className) {
            super(context, className);
        }

        public ParseUserLetterQueryAdapter(Context context, Class clazz, int itemViewResource) {
            super(context, clazz, itemViewResource);
        }

        public ParseUserLetterQueryAdapter(Context context, String className, int itemViewResource) {
            super(context, className, itemViewResource);
        }

        public ParseUserLetterQueryAdapter(Context context, QueryFactory queryFactory) {
            super(context, queryFactory);
        }

        public ParseUserLetterQueryAdapter(Context context, QueryFactory queryFactory, int itemViewResource) {
            super(context, queryFactory, itemViewResource);
        }

        public View getItemView(ParseObject object, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.row_small_user_letter, null);
            }
            TextView tv_score = (TextView) v.findViewById(R.id.tv_votes);
            tv_score.setText(String.valueOf(object.getInt("popularity")));
            TextView tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_title.setText(object.getString("Recipient"));
            return v;
        }
    }

    private class DownloadUserPicture extends AsyncTask<Void,Void,Bitmap>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return UserDao.instance().getSignaturePicture(selectedUser.getPublicSignature());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            getActivity().setProgressBarIndeterminateVisibility(false);
            if(iv_profilePicture != null && bitmap != null){
                iv_profilePicture.setImageBitmap(bitmap);
            }
        }
    }
}