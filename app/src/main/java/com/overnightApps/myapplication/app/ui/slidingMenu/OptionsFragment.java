package com.overnightApps.myapplication.app.ui.slidingMenu;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.overnightApps.myapplication.app.R;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuItem;
import com.overnightApps.myapplication.app.ui.optionsMenus.MenuOptions;
import com.overnightApps.myapplication.app.service.UserSession;
import com.overnightApps.myapplication.app.ui.homeFragments.homeActivityInteractionInterfaces.OnLoginFragmentListener;
import com.overnightApps.myapplication.app.ui.optionsMenus.menuOptionFactories.HomeMenuOptionsFactory;

import junit.framework.Assert;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link com.overnightApps.myapplication.app.ui.slidingMenu.OptionsFragment.OnHomeMenuClickListener}
 * interface.
 */
public class OptionsFragment extends Fragment implements AbsListView.OnItemClickListener {
    private static final String USER_SERVICE_ARG = "UserServiceArg";
    private ListAdapter listAdapter;
    private MenuOptions homeMenuOptions;
    private OnLoginFragmentListener onLogInFragmentListener;
    private OnHomeMenuClickListener onHomeMenuClickListener;

    public OptionsFragment() {
    }

    public static OptionsFragment newInstance(UserSession userSession) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(USER_SERVICE_ARG, userSession);
        OptionsFragment optionsFragment = new OptionsFragment();
        optionsFragment.setArguments(arguments);
        return optionsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assert.assertNotNull(getArguments());
        UserSession userSession = (UserSession) getArguments().getSerializable(USER_SERVICE_ARG);
        Assert.assertNotNull(userSession);
        homeMenuOptions = new HomeMenuOptionsFactory(userSession.getCurrentUser()).newInstance();
        listAdapter = new ArrayAdapter<MenuItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, homeMenuOptions.getItems());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);
        AbsListView mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onHomeMenuClickListener = (OnHomeMenuClickListener) activity;
        onLogInFragmentListener = (OnLoginFragmentListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onHomeMenuClickListener = null;
        onLogInFragmentListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (homeMenuOptions.getItems().get(position).id == HomeMenuOptionsFactory.LOGIN_MENU_OPTION_ID) {
            onLogInFragmentListener.startLoginDialog();
        }

        if (null != onHomeMenuClickListener) {
            onHomeMenuClickListener.optionSelected(homeMenuOptions.getItems().get(position).id);
        }
    }

    public interface OnHomeMenuClickListener {
        public void optionSelected(int optionId);
    }
}