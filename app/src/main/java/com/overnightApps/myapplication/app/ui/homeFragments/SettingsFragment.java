package com.overnightApps.myapplication.app.ui.homeFragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnUserLogOutListener;
import com.overnightApps.myapplication.app.util.AUtil;
import com.overnightApps.myapplication.app.util.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnUserLogInListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends PreferenceFragment {
    @InjectView(R.id.b_logOut)
    Button b_logOut;
    private OnUserLogOutListener onUserLogOutListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_layout, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.b_logOut)
    public void logInClick(final Button button) {
        AUtil.confirmUserSelection(getActivity(), new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               switch (which) {
                   case DialogInterface.BUTTON_POSITIVE:
                       Logger.p(getActivity(), "Logged out button clicked");
                       onUserLogOutListener.logCurrentUserOut();
                       break;
                   case DialogInterface.BUTTON_NEGATIVE:
                       break;
               }
           }
       });
     }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onUserLogOutListener = (OnUserLogOutListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onUserLogOutListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
