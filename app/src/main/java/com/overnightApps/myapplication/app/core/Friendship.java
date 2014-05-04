package com.overnightApps.myapplication.app.core;

import java.io.Serializable;

/**
 * Created by andre on 3/25/14.
 */
public class Friendship implements Serializable {
    private User current;
    private User friend;
    private boolean isTrusted;

    public Friendship(User current, User friend, boolean isTrusted) {
        this.current = current;
        this.friend = friend;
        this.isTrusted = isTrusted;
    }

    public void trustUser() {
        isTrusted = true;
    }

    public void unTrustUser() {
        isTrusted = false;
    }

    public User getCurrent() {
        return current;
    }

    public void setCurrent(User current) {
        this.current = current;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public boolean isTrusted() {
        return isTrusted;
    }

    public void setTrusted(boolean isTrusted) {
        this.isTrusted = isTrusted;
    }
}
