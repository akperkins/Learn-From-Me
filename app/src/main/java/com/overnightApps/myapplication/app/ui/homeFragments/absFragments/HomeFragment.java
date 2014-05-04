package com.overnightApps.myapplication.app.ui.homeFragments.absFragments;


import android.app.Activity;
import android.app.Fragment;
import android.util.Log;

import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnActiveFragmentListener;

/**
 * Created by andre on 3/15/14.
 */
public abstract class HomeFragment extends Fragment {
    private OnActiveFragmentListener onActiveFragmentListener;

    protected abstract int getFragmentId();

    @Override
    public void onResume() {
        super.onResume();
        onActiveFragmentListener.setActiveFragment(getFragmentId());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onActiveFragmentListener = (OnActiveFragmentListener) activity;
        } catch (ClassCastException e) {
            Log.e("TAG", "Calling activity does not implement OnActiveFragmentListener", e);
            throw e;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onActiveFragmentListener = null;
    }
}
