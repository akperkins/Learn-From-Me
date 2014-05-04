package com.overnightApps.myapplication.app.ui.homeFragments.UserLogInStateDependentFragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.service.UserSession;
import com.overnightApps.myapplication.app.ui.FragmentIds;
import com.overnightApps.myapplication.app.ui.homeFragments.absFragments.HomeFragment;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnLetterDetailActionListener;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnUserFragmentListener;

import junit.framework.Assert;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A fragment representing a single Letter detail screen.
 * This fragment is either contained in a {@link com.overnightApps.myapplication.app.ui.HomeActivity}
 * in two-pane mode
 * on handsets.
 */
public abstract class LetterDetailFragment extends HomeFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    protected static final String ARG_LETTER = "item_letter_id";
    protected static final String USER_SERVICE_ARG = "UserServiceARG";
    protected static final String LETTER_SERVICE_ARG = "letterDaoArg";

    @InjectView(R.id.tv_recipient)
    TextView tv_recipient;
    @InjectView(R.id.tv_message)
    TextView tv_message;
    @InjectView(R.id.tv_signature)
    TextView tv_signature;
    @InjectView(R.id.ib_upVote)
    ImageButton ib_upVote;
    @InjectView(R.id.ib_downVote)
    ImageButton ib_downVote;
    @InjectView(R.id.tv_votes)
    TextView tv_votes;
    @InjectView(R.id.ll_voteSystem)
    LinearLayout ll_voteSystem;
    @InjectView(R.id.tv_thanks)
    TextView tv_thanks;

    protected Letter letter;

    protected UserSession userSession;
    protected OnLetterDetailActionListener onLetterDetailActions;
    protected OnUserFragmentListener onUserFragmentListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LetterDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assert.assertNotNull(getArguments());

        letter = (Letter) getArguments().getSerializable(ARG_LETTER);
        Assert.assertNotNull(letter);

        userSession = (UserSession) getArguments().getSerializable(USER_SERVICE_ARG);
        Assert.assertNotNull(userSession);
    }

    @Override
    public int getFragmentId() {
        return FragmentIds.LETTER_DETAIL.getId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_letter_detail, container, false);
        setHasOptionsMenu(true);

        ButterKnife.inject(this, rootView);
        tv_recipient.setText(letter.getRecipient());
        tv_message.setText(letter.getMessage());
        tv_signature.setText(letter.getSignature());
        tv_votes.setText(String.valueOf(letter.getVotes()));

        if(letter.isSignedPublicly()){
            tv_signature.setTextColor(getResources().getColor(R.color.background_selected));
        }

        return rootView;
    }

    protected void refreshScoreDisplay() {
        tv_votes.setText("Score: " + letter.getVotes());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onLetterDetailActions = (OnLetterDetailActionListener) activity;
        onUserFragmentListener = (OnUserFragmentListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onLetterDetailActions = null;
        onUserFragmentListener = null;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.letter_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @OnClick(R.id.tv_signature)
    public void showLetterCreator(TextView textView) {
        if(letter.isSignedPublicly()) {
            onUserFragmentListener.userButtonClick(letter.getCreator());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_letter_share:
                onLetterDetailActions.sendRecommendation(letter);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}