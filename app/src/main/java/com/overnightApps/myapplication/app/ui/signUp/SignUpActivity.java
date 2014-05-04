package com.overnightApps.myapplication.app.ui.signUp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.view.Window;

import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.service.SignUpFormVerifier;
import com.overnightApps.myapplication.app.service.SignUpSubmission;
import com.overnightApps.myapplication.app.service.SignUpSubmissionBuilder;
import com.overnightApps.myapplication.app.ui.HomeActivity;
import com.overnightApps.myapplication.app.util.Logger;
import com.overnightApps.myapplication.app.util.MyAssert;

import junit.framework.Assert;

import java.util.Locale;

public class SignUpActivity extends Activity implements OnSignUpListener, OnUserSelectSignUpMethodListener{

    private static final String CALLED_REQUEST_WINDOW_FEATURE = "calledRequestWindowFeature";
    private static final int SELECT_PROFILE_PICTURE_FRAGMENT = 2;
    private static final int SELECT_USER_FORM_FRAGMENT = 1;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private NoSwipingViewPager mViewPager;
    private boolean isProgressCircleSetupCodeCalled;
    private SignUpSubmissionBuilder signUpSubmissionBuilder;
    private SignUpSubmission signUpSubmission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            isProgressCircleSetupCodeCalled = savedInstanceState.getBoolean(CALLED_REQUEST_WINDOW_FEATURE);
        }
        else{
            isProgressCircleSetupCodeCalled = false;
        }
        setUpProgressCircleOnActionBar();
        setContentView(R.layout.activity_sign_up);

        mSectionsPagerAdapter = new SectionsPagerAdapter(this.getFragmentManager());

        mViewPager = (NoSwipingViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        signUpSubmissionBuilder = new SignUpSubmissionBuilder();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(CALLED_REQUEST_WINDOW_FEATURE, isProgressCircleSetupCodeCalled);
    }

    private void setUpProgressCircleOnActionBar() {
        if(!isProgressCircleSetupCodeCalled) {
            this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            isProgressCircleSetupCodeCalled = true;
        }
    }

    @Override
    public void provideUserInformation(SignUpFormVerifier signUpFormVerifier) {
        Assert.assertNotNull(signUpFormVerifier);
        signUpSubmissionBuilder.setSignUpForm(signUpFormVerifier.getSignUpForm());
        mViewPager.setCurrentItem(SELECT_PROFILE_PICTURE_FRAGMENT);
    }

    @Override
    public void profilePictureSelected(Bitmap bitmap) {
        signUpSubmissionBuilder.setProfilePicture(bitmap);
        SignUpSubmission signUpSubmission = signUpSubmissionBuilder.createSignUpSubmission();
        new SignUpAsyncTask().execute(signUpSubmission);
    }

    private void startHomeActivity() {
        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }

    @Override
    public void startEmailProcess() {
        mViewPager.setCurrentItem(SELECT_USER_FORM_FRAGMENT);
    }

    private class SignUpAsyncTask extends AsyncTask<SignUpSubmission,Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Boolean doInBackground(SignUpSubmission... params) {
            return params[0].signUp();
        }

        @Override
        protected void onPostExecute(Boolean isSignUpSuccessful) {
            super.onPostExecute(isSignUpSuccessful);
            setProgressBarIndeterminateVisibility(false);
            if(isSignUpSuccessful) {
                SignUpActivity.this.startHomeActivity();
                Logger.p(SignUpActivity.this,"Welcome!");
            }else{
                Logger.p(SignUpActivity.this,"Error while signing up.Please try again later.");
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return SelectSignUpMethodFragment.newInstance();
            }
            if(position == 1) {
                return UserSignUpFormFragment.newInstance();
            }
            if(position == 2){
                return UploadProfilePictureFragment.newInstance();
            }
            MyAssert.assertShouldNotReachHere();
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Start Log-In process".toUpperCase(l);
                case 1:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }
}