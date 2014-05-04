package com.overnightApps.myapplication.app.ui.signUp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.core.helper.SignUpForm;
import com.overnightApps.myapplication.app.service.SignUpFormVerifier;
import com.overnightApps.myapplication.app.util.AUtil;
import com.overnightApps.myapplication.app.util.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserSignUpFormFragment extends Fragment {
    @InjectView(R.id.et_fullName)
    EditText et_fullName;
    @InjectView(R.id.et_signUp_email)
    EditText et_email;
    @InjectView(R.id.et_signUp_password)
    EditText et_password;
    @InjectView(R.id.et_signUp_private_Signature)
    EditText et_privateSignature;
    @InjectView(R.id.et_signUp_public_signature)
    EditText et_publicSignature;
    @InjectView(R.id.b_signUp_signUp)
    Button b_singUp;
    private OnSignUpListener onSignUpListener;


    public UserSignUpFormFragment() {
    }

    public static UserSignUpFormFragment newInstance() {
        UserSignUpFormFragment fragment = new UserSignUpFormFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @OnClick(R.id.b_signUp_signUp)
    public void attemptSignUp(Button button) {
        String fullName = AUtil.extractString(et_fullName);
        String email = AUtil.extractString(et_email);
        String password = AUtil.extractString(et_password);
        String privateSignature = AUtil.extractString(et_privateSignature);
        String publicSignature = AUtil.extractString(et_publicSignature);

        SignUpForm signUpForm = new SignUpForm(fullName,email,password,publicSignature,privateSignature);

        SignUpFormVerifier signUpFormVerifier = SignUpFormVerifier.newInstance(signUpForm);
        final SignUpFormVerifier.StatusCode statusCode = signUpFormVerifier.performClientSideChecks();
        Logger.p(getActivity(), statusCode.getMessage());
        if(statusCode == SignUpFormVerifier.StatusCode.PASSED_INITIAL_CHECKS){
            onSignUpListener.provideUserInformation(signUpFormVerifier);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onSignUpListener = (OnSignUpListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSignUpListener = null;
    }
}
