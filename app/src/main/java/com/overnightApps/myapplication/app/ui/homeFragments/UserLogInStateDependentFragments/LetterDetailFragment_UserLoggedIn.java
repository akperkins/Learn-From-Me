package com.overnightApps.myapplication.app.ui.homeFragments.UserLogInStateDependentFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.core.LetterVote;
import com.overnightApps.myapplication.app.service.LetterVoter;
import com.overnightApps.myapplication.app.service.UserSession;

import butterknife.OnClick;

/**
 * Created by andre on 4/24/14.
 */
public class LetterDetailFragment_UserLoggedIn extends LetterDetailFragment {
    protected LetterVoter letterVoter;
    public static final String LETTER_VOTE_ARG = "userVoteOnLetterArg";


    public static LetterDetailFragment newInstance(Letter letter, UserSession userSession,
                                                   LetterVote letterVote) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_LETTER, letter);
        arguments.putSerializable(USER_SERVICE_ARG, userSession);
        arguments.putSerializable(LETTER_VOTE_ARG, letterVote);
        LetterDetailFragment letterDetailFragment = new LetterDetailFragment_UserLoggedIn();
        letterDetailFragment.setArguments(arguments);
        return letterDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        refreshRatingButtonsText();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LetterVote letterVote = (LetterVote) getArguments().getSerializable(LETTER_VOTE_ARG);
        letterVoter = LetterVoter.newInstance(letter, userSession.getCurrentUser(), letterVote);
    }


    private void refreshRatingButtonsText() {
        LetterVote.Vote currentVote = letterVoter.getCurrentVote();
        if(currentVote == null){
            ll_voteSystem.setVisibility(View.VISIBLE);
            tv_thanks.setVisibility(View.GONE);
        }
        else{
            tv_thanks.setVisibility(View.VISIBLE);
            ll_voteSystem.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.ib_upVote)
    public void upVoteLetter(ImageButton imageButton){
        letterVoter.voteLetterUp();
        refreshScoreDisplay();
        refreshRatingButtonsText();
    }

    @OnClick(R.id.ib_downVote)
    public void downVoteLetter(ImageButton imageButton){
        letterVoter.voteLetterDown();
        refreshScoreDisplay();
        refreshRatingButtonsText();
    }

    @Override
    public void onStop() {
        super.onStop();
        letterVoter.saveChanges();
    }
}