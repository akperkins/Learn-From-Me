package com.overnightApps.myapplication.app.ui.homeFragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.core.Letter;
import com.overnightApps.myapplication.app.service.LetterSender;
import com.overnightApps.myapplication.app.service.UserSession;
import com.overnightApps.myapplication.app.ui.FragmentIds;
import com.overnightApps.myapplication.app.ui.homeFragments.absFragments.HomeFragment;
import com.overnightApps.myapplication.app.util.AUtil;
import com.overnightApps.myapplication.app.util.Logger;
import com.overnightApps.myapplication.app.util.MyAssert;

import junit.framework.Assert;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateLetterFragment extends HomeFragment {

    private static final String USER_SERVICE_ARG = "userServiceArg";
    private static final String LETTER_SERVICE_ARG = "letterServiceArg";
    @InjectView(R.id.et_shareStory)
    EditText et_shareStory;
    @InjectView(R.id.ib_recipients)
    ImageButton ib_recipients;
    @InjectView(R.id.et_recipients)
    EditText et_recipients;
    private UserSession userSession;
    private Letter originalLetter;

    public CreateLetterFragment() {
    }

    public static Fragment newInstance(UserSession userSession, Letter original) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(USER_SERVICE_ARG, userSession);
        arguments.putSerializable(LETTER_SERVICE_ARG, original);
        CreateLetterFragment createLetterFragment = new CreateLetterFragment();
        createLetterFragment.setArguments(arguments);
        return createLetterFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assert.assertNotNull(getArguments());
        userSession = (UserSession) getArguments().getSerializable(USER_SERVICE_ARG);
        originalLetter = (Letter) getArguments().getSerializable(LETTER_SERVICE_ARG);
        Assert.assertNotNull(userSession);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_letter, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @OnClick(R.id.ib_recipients)
    public void toggleRecipients(ImageButton imageButton){
        if(et_recipients.getVisibility()==View.VISIBLE){
            et_recipients.setVisibility(View.INVISIBLE);
        }else if(et_recipients.getVisibility()==View.INVISIBLE){
            et_recipients.setVisibility(View.VISIBLE);
        }else{
            MyAssert.assertShouldNotReachHere();
        }
    }

    @OnClick(R.id.ib_signature)
    public void signLetter(ImageButton imageButton){
       final String initialRecipients = cleanRecipients((AUtil.extractString(et_recipients)));
       final String message = AUtil.extractString(et_shareStory);
       if(isBlank(message)){
           Toast.makeText(getActivity(), "Your message can not be empty", Toast.LENGTH_LONG).show();
       }
       final DialogInterface.OnClickListener onClickListener =  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE || which == DialogInterface.BUTTON_NEGATIVE){
                    LetterSender letterSender;
                    if(which == DialogInterface.BUTTON_POSITIVE){
                        letterSender = LetterSender.newPublicInstance(originalLetter,
                                userSession.getCurrentUser());
                    }else{
                        letterSender = LetterSender.newPrivateInstance(originalLetter,
                                userSession.getCurrentUser());
                    }
                    letterSender.sendLetter(initialRecipients, message);
                    Logger.p(getActivity(),"Letter posted publicly");
                    getActivity().getFragmentManager().popBackStack();
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Is Your letter ready to be signed and sent?").setPositiveButton(("Sign Publicly"),
                onClickListener)
                .setNeutralButton("Cancel", onClickListener)
                .setNegativeButton("Sign Privately",onClickListener)
                .show();
    }

    private String cleanRecipients(String initialRecipients) {
        final String finalRecipients;
        if(isBlank(initialRecipients)){
            finalRecipients = "Learn From Me";
        }else {
            finalRecipients = initialRecipients;
        }
        return finalRecipients;
    }

    private boolean isBlank(String recipients) {
        return recipients.equals("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public int getFragmentId() {
        return FragmentIds.CREATE_LETTER.getId();
    }
}