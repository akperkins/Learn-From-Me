package com.overnightApps.myapplication.app.ui.homeFragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.dao.LetterDao;
import com.overnightApps.myapplication.app.service.UserSession;
import com.overnightApps.myapplication.app.ui.FragmentIds;
import com.overnightApps.myapplication.app.ui.homeFragments.absFragments.FilterListFragment;
import com.overnightApps.myapplication.app.ui.homeFragments.asyncTasks.LoadDetailFragmentTask;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnCreateLetterFragmentListener;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptions;
import com.overnightApps.myapplication.app.ui.optionsMenus.menuOptionFactories.LetterMenuOptionsFactory;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import junit.framework.Assert;

/**
 * A list fragment representing a list of Letters. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link com.overnightApps.myapplication.app.ui.homeFragments.UserLogInStateDependentFragments.LetterDetailFragment}.
 * <p/>
 */
public class LearnFragment extends FilterListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private static final String USER_SERVICE_ARGUMENT = "UserService Argument";

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private OnCreateLetterFragmentListener onCreateLetterFragmentListener;

    private UserSession userSession;

    private ParseQuery<ParseObject> query;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LearnFragment() {
    }

    public static LearnFragment newInstance(UserSession userSession) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(USER_SERVICE_ARGUMENT, userSession);
        LearnFragment learnFragment = new LearnFragment();
        learnFragment.setArguments(arguments);
        return learnFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        Assert.assertNotNull(arguments);
        userSession = (UserSession)arguments.getSerializable(USER_SERVICE_ARGUMENT);
        Assert.assertNotNull(userSession);
    }

    @Override
    public int getFragmentId() {
        return FragmentIds.LEARN.getId();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        LoadDetailFragmentTask loadDetailFragmentTask = new LoadDetailFragmentTask();
        loadDetailFragmentTask.setActivity(getActivity());
        loadDetailFragmentTask.setUserSession(userSession);
        loadDetailFragmentTask.execute(adapter.getItem(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onCreateLetterFragmentListener = (OnCreateLetterFragmentListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onCreateLetterFragmentListener = null;
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */


    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_createLetter:
                onCreateLetterFragmentListener.createLetterButtonClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public ActionBar.OnNavigationListener getMenuOnNavigationListener() {
        return new LetterMenuOnNavigationListener();
    }

    @Override
    public MenuOptions getMenuOptions() {
        return new LetterMenuOptionsFactory(userSession.getCurrentUser()).newInstance();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.learn_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private class LetterMenuOnNavigationListener implements ActionBar.OnNavigationListener {
        @Override
        public boolean onNavigationItemSelected(int itemPosition, long itemId) {
             //configure query
            if (itemId == LetterMenuOptionsFactory.NEWEST_MENU_OPTION_ID) {
                query = new ParseQuery(LetterDao.CLASS_NAME);
                query.orderByDescending(LetterDao.CREATED_AT_TAG);
                 adapter =
                        new ParseLetterQueryAdapter(getActivity(),
                                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                                    public ParseQuery<ParseObject> create() {
                                        return query;
                                    }
                                });
            } else {//default is the popular choice
                query = new ParseQuery(LetterDao.CLASS_NAME);
                query.orderByDescending(LetterDao.POPULARITY);
                 adapter =
                        new ParseLetterQueryAdapter(getActivity(),
                                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                                    public ParseQuery<ParseObject> create() {
                                        return query;
                                    }
                                });
            }
            SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
            swingBottomInAnimationAdapter.setInitialDelayMillis(300);
            swingBottomInAnimationAdapter.setAbsListView(getListView());

            setListAdapter(swingBottomInAnimationAdapter);

            return true;
        }
    }

    /**
     * Created by andre on 2/26/14.
     */
    public static class ParseLetterQueryAdapter extends ParseQueryAdapter<ParseObject> {
        public ParseLetterQueryAdapter(Context context, Class clazz) {
            super(context, clazz);
        }

        public ParseLetterQueryAdapter(Context context, String className) {
            super(context, className);
        }

        public ParseLetterQueryAdapter(Context context, Class clazz, int itemViewResource) {
            super(context, clazz, itemViewResource);
        }

        public ParseLetterQueryAdapter(Context context, String className, int itemViewResource) {
            super(context, className, itemViewResource);
        }

        public ParseLetterQueryAdapter(Context context, QueryFactory queryFactory) {
            super(context, queryFactory);
        }

        public ParseLetterQueryAdapter(Context context, QueryFactory queryFactory, int itemViewResource) {
            super(context, queryFactory, itemViewResource);
        }

        public View getItemView(ParseObject object, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.activity_googlecards_card, null);
            }
            TextView recipient = (TextView) v.findViewById(R.id.tv_recipient);
            recipient.setText("Dear, " + object.getString(LetterDao.RECIPIENT));
            TextView message = (TextView) v.findViewById(R.id.tv_message);
            message.setText(object.getString(LetterDao.MESSAGE));
            TextView signature = (TextView) v.findViewById(R.id.tv_signature);
            signature.setText(object.getString(LetterDao.SIGNATURE));

            TextView tv_score = (TextView) v.findViewById(R.id.tv_votes);
            tv_score.setText(String.valueOf(object.getInt(LetterDao.VOTES)));

            return v;
        }
    }
}