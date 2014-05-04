package com.overnightApps.myapplication.app.ui.homeFragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.core.Comment;
import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.dao.LetterDao;
import com.overnightApps.myapplication.app.service.UserSession;
import com.overnightApps.myapplication.app.ui.FragmentIds;
import com.overnightApps.myapplication.app.ui.homeFragments.absFragments.HomeFragment;
import com.overnightApps.myapplication.app.util.AUtil;

import junit.framework.Assert;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 */
public class CommentsFragment extends HomeFragment {

    private static final String USER_SERVICE_ARG = "userServiceArg";
    private static final String LETTER_SERVICE_ARG = "letterService";
    @InjectView(R.id.lv_comments)
    ListView lv_comments;
    @InjectView(R.id.et_addComment)
    EditText et_addComment;

    private static final String LETTER_ARG = "letterArg";
    private Letter letter;
    private UserSession userSession;
    private ListAdapter commentsAdapter;

    public static CommentsFragment newInstance(UserSession userSession,Letter letter) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(LETTER_ARG,letter);
        arguments.putSerializable(USER_SERVICE_ARG, userSession);
        fragment.setArguments(arguments);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CommentsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assert.assertNotNull(getArguments());
        letter = (Letter)getArguments().getSerializable(LETTER_ARG);
        userSession = (UserSession)getArguments().getSerializable(USER_SERVICE_ARG);
        Assert.assertNotNull(letter);
        Assert.assertNotNull(userSession);
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.comments_fragment, container, false);
        ButterKnife.inject(this, v);
        commentsAdapter = new ArrayAdapter<Comment>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, letter.getCommentList());
        et_addComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    postComment();
                    return true;
                }
                return false;
            }
        });
        lv_comments.setAdapter(commentsAdapter);
        return v;
    }

    void postComment(){
        String message = AUtil.extractString(et_addComment);
        letter.addComment(message, userSession.getCurrentUser());
        et_addComment.setText("");
        commentsAdapter = new ArrayAdapter<Comment>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, letter.getCommentList());
        lv_comments.setAdapter(commentsAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LetterDao.instance().save(letter);
    }

    @Override
    public int getFragmentId() {
        return FragmentIds.COMMENT.getId();
    }
}