package com.overnightApps.myapplication.app.ui.signUp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.overnightApps.myapplication.app.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 4/25/14.
 */
public class SelectSignUpMethodFragment extends Fragment {
    OnUserSelectSignUpMethodListener onUserSelectSignUpMethodListener;
    @InjectView(R.id.b_signUpWithEmail)
    Button b_signUpWithEmail;

    public static Fragment newInstance() {
        return new SelectSignUpMethodFragment();
    }

    public SelectSignUpMethodFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_sign_up_method_fragment,container,false);
        ButterKnife.inject(this,view);
        return view;
    }

    @OnClick(R.id.b_signUpWithEmail)
    public void signUpWithEmail(Button button){
        onUserSelectSignUpMethodListener.startEmailProcess();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onUserSelectSignUpMethodListener = (OnUserSelectSignUpMethodListener)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onUserSelectSignUpMethodListener = null;
    }
}
