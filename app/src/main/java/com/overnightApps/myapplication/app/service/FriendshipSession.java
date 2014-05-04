package com.overnightApps.myapplication.app.service;

import com.overnightApps.myapplication.app.core.FriendShipRequest;
import com.overnightApps.myapplication.app.core.Friendship;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.FriendshipDao;
import com.overnightApps.myapplication.app.dao.FriendshipRequestDao;

/**
 * Created by andre on 3/25/14.
 */
public class FriendshipSession {
    private final FriendshipRequestDao friendshipRequestDao;
    private final FriendshipDao friendshipDao;
    private final User currentUser;
    private final User friend;

    public FriendshipSession(FriendshipRequestDao friendshipRequestDao, FriendshipDao friendshipDao, User currentUser, User friend) {
        this.friendshipRequestDao = friendshipRequestDao;
        this.friendshipDao = friendshipDao;
        this.currentUser = currentUser;
        this.friend = friend;
    }

    public static FriendshipSession friendshipServiceInstance(User current, User friend) {
        return new FriendshipSession(FriendshipRequestDao.instance(), FriendshipDao.instance(), current, friend);
    }

    public FriendShipState getFriendshipState() {
        FriendShipRequest friendShipRequest = friendshipRequestDao.get(currentUser, friend);
        if (isFriendshipRequestFound(friendShipRequest)) {
            return FriendShipState.NONE;
        }
        if (!friendShipRequest.isResponded()) {
            if (isCurrentUserRequestSender(friendShipRequest)) {
                return FriendShipState.WAITING_FOR_RESPONSE;
            } else {
                return FriendShipState.RECEIVED_REQUEST;
            }
        } else {
            if (!friendShipRequest.isAccepted()) {
                return FriendShipState.DENIED;
            } else if (isFriendTrusted(friend)) {
                return FriendShipState.TRUSTED_FRIEND;
            } else {
                return FriendShipState.UNTRUSTED_FRIEND;
            }
        }
    }

    public void sendFriendRequest() {
        friendshipRequestDao.save(new FriendShipRequest(friend,currentUser, false, false));
    }

    public void acceptFriendRequest() {
        FriendShipRequest friendShipRequest = friendshipRequestDao.get(currentUser, friend);
        friendShipRequest.acceptFriendRequest();
        friendshipRequestDao.save(friendShipRequest);
        Friendship newFriendShip1 = new Friendship(currentUser,friend,false);
        friendshipDao.save(newFriendShip1);
        Friendship newFriendShip2 = new Friendship(friend,currentUser,false);
        friendshipDao.save(newFriendShip2);
    }

    public void denyFriendRequest() {
        FriendShipRequest friendShipRequest = friendshipRequestDao.get(currentUser, friend);
        friendShipRequest.denyFriendRequest();
        friendshipRequestDao.save(friendShipRequest);
    }

    public void trustUser() {
        Friendship friendship = friendshipDao.getFriendship(currentUser, friend);
        friendship.trustUser();
        friendshipDao.save(friendship);
    }

    public void unTrustUser() {
        Friendship friendship = friendshipDao.getFriendship(currentUser, friend);
        friendship.unTrustUser();
        friendshipDao.save(friendship);
    }

    private boolean isFriendTrusted(User friend) {
        return friendshipDao.getFriendship(currentUser, friend).isTrusted();
    }

    private boolean isFriendshipRequestFound(FriendShipRequest friendShipRequest) {
        return friendShipRequest == null;
    }

    private boolean isCurrentUserRequestSender(FriendShipRequest friendShipRequest) {
        return friendShipRequest.getFrom().equals(currentUser);
    }

    public boolean isFriendTrusted() {
        Friendship friendship = friendshipDao.getFriendship(currentUser,friend);
        return friendship != null && friendship.isTrusted();
    }

    public enum FriendShipState {
        TRUSTED_FRIEND("Un-trust User"), UNTRUSTED_FRIEND("Trust User"),
        WAITING_FOR_RESPONSE("Waiting For Response"), RECEIVED_REQUEST("Accept Friend Request"),
        NONE("Add as a Friend"), DENIED("You Denied Friend Request");
        final String getMessageForUser;

        FriendShipState(String getMessageForUser) {
            this.getMessageForUser = getMessageForUser;
        }

        public String getMessageForUser() {
            return getMessageForUser;
        }
    }
}
