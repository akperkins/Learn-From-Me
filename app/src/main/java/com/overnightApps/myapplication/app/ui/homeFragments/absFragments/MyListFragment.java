package com.overnightApps.myapplication.app.ui.homeFragments.absFragments;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnActiveFragmentListener;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptions;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by andre on 3/15/14.
 */
public abstract class  MyListFragment extends ListFragment {
    //TODO - this class is deprecated, all fragments should extend HomeFragment

    private PullToRefreshLayout mPullToRefreshLayout;
    private OnActiveFragmentListener onActiveFragmentListener;

    protected abstract int getFragmentId();

    @Override
    public void onResume() {
        super.onResume();
        onActiveFragmentListener.setActiveFragment(getFragmentId());
    }

    void setUpMenuOptionsOnActionBar(MenuOptions menuOptions, ActionBar.OnNavigationListener onNavigationListener) {
        ActionBar bar = getActivity().getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, menuOptions.getItems());

        bar.setListNavigationCallbacks(arrayAdapter, onNavigationListener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onActiveFragmentListener = (OnActiveFragmentListener) activity;
        }catch (ClassCastException e){
            Log.e("TAG","Calling activity does not implement OnActiveFragmentListener",e);
            throw e;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onActiveFragmentListener = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      /*  // This is the View which is created by ListFragment
        ViewGroup viewGroup = (ViewGroup) view;

        // We need to create a PullToRefreshLayout manually
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

        // We can now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())

                // We need to insert the PullToRefreshLayout into the Fragment's ViewGroup
                .insertLayoutInto(viewGroup)

                        // We need to mark the ListView and it's Empty View as pullable
                        // This is because they are not dirent children of the ViewGroup
                .theseChildrenArePullable(getListView(), getListView().getEmptyView())

                        // We can now complete the setup as desired
                .listener(new MyOnRefreshListener())
                .options(Options.create()
                        // Here we make the refresh scroll distance to 75% of the refreshable view's height
                        .scrollDistance(.25f)
                        .build())
                .setup(mPullToRefreshLayout);*/
    }

    protected void removeThisFragmentFromStack() {
        getActivity().getFragmentManager().popBackStack();
    }

    private class MyOnRefreshListener implements OnRefreshListener {
        @Override
        public void onRefreshStarted(View view) {
            // Hide the list
            setListShown(false);

            /**
             * Simulate Refresh with 4 seconds sleep
             */
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);

                    // Notify PullToRefreshLayout that the refresh has finished
                    mPullToRefreshLayout.setRefreshComplete();

                    if (getView() != null) {
                        // Show the list again
                        setListShown(true);
                    }
                }
            }.execute();
        }
    }
}
