package com.overnightApps.myapplication.app.ui.homeFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.dao.UserDao;
import com.overnightApps.myapplication.app.service.UserLogIn;
import com.overnightApps.myapplication.app.service.UserSession;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnSignUpActivityListener;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnUserLogInListener;
import com.overnightApps.myapplication.app.util.AUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 3/26/14.
 */
public class LogInDialogFragment extends DialogFragment {//TODO - replace this with a real fragment
    @InjectView(R.id.et_email)
    EditText et_email;
    @InjectView(R.id.et_password)
    EditText et_password;
    @InjectView(R.id.tv_startSignUp)
    TextView tv_startSignUp;
    private OnUserLogInListener onUserLogInListener;
    private OnSignUpActivityListener onSignUpActivityListener;

    public LogInDialogFragment() {
    }

    public static LogInDialogFragment newInstance(UserSession userSession) {
        Bundle arguments = new Bundle();
        LogInDialogFragment logInDialogFragment = new LogInDialogFragment();
        logInDialogFragment.setArguments(arguments);
        return logInDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.log_in_dialog, null);
        ButterKnife.inject(this, view);
        builder.setView(view)
                .setPositiveButton(getString(R.string.logInButtonText), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = AUtil.extractString(et_email);
                        String password = AUtil.extractString(et_password);
                             UserLogIn userLogIn = new UserLogIn(email,password,UserDao.instance());
                             UserLogIn.StatusCode statusCode = userLogIn.logUserIn();
                             if (statusCode == UserLogIn.StatusCode.SUCCESS) {
                                dismiss();
                                onUserLogInListener.logCurrentUserIn();
                            }else {
                                 Toast.makeText(getActivity(), statusCode.getMessage(),Toast.LENGTH_LONG)
                                         .show();
                             }
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    @OnClick(R.id.tv_startSignUp)
    public void startSignUp(TextView textView) {
        onSignUpActivityListener.startSignUpProcess();
        dismiss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onUserLogInListener = (OnUserLogInListener) activity;
        onSignUpActivityListener = (OnSignUpActivityListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onUserLogInListener = null;
        onSignUpActivityListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
