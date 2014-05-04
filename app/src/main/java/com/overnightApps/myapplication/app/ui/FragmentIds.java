package com.overnightApps.myapplication.app.ui;

/**
 * Created by andre on 3/26/14.
 */
public enum FragmentIds {
    CREATE_LETTER(0), FRIEND(1), LEARN(2), LETTER_DETAIL(3), USER(4), SETTINGS(5), LOG_IN(6),
    RECEIVE_RECOMMENDATIONS(7), REPLIES(8), COMMENT(9), SEND_RECOMMENDATION(10), FRIEND_REQUESTS(11);
    private final int id;

    FragmentIds(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
