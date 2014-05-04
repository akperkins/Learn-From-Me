package com.overnightApps.myapplication.app.core;

import java.io.Serializable;

/**
 * Created by andre on 3/25/14.
 */
public class FriendShipRequest implements Serializable {
    private User to;
    private User from;
    private boolean isAccepted;
    private boolean isResponded;

    public FriendShipRequest(User to, User from, boolean isAccepted, boolean isResponded) {
        this.to = to;
        this.from = from;
        this.isAccepted = isAccepted;
        this.isResponded = isResponded;
    }

    public void acceptFriendRequest() {
        isResponded = true;
        isAccepted = true;
    }

    public void denyFriendRequest() {
        isResponded = true;
        isAccepted = false;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public boolean isResponded() {
        return isResponded;
    }

    public void setResponded(boolean isResponded) {
        this.isResponded = isResponded;
    }
}
