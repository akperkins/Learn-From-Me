package com.overnightApps.myapplication.app.ui.homeFragments.absFragments;

import android.app.ActionBar;
import android.os.Bundle;

import com.overnightApps.myapplication.app.ui.homeFragments.LearnFragment;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptions;

import butterknife.ButterKnife;

/**
 * Created by andre on 3/16/14.
 */
public abstract class  FilterListFragment extends MyListFragment {
    /**
     * The adapter handed to the list view
     */
    protected LearnFragment.ParseLetterQueryAdapter adapter;

    public void onResume() {
        super.onResume();
        setUpMenuOptionsOnActionBar(getMenuOptions(), getMenuOnNavigationListener());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    protected abstract ActionBar.OnNavigationListener getMenuOnNavigationListener();

    protected abstract MenuOptions getMenuOptions();
}
