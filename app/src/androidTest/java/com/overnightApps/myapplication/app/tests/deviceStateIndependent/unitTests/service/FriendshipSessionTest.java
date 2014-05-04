package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.service;

import com.overnightApps.myapplication.app.core.FriendShipRequest;
import com.overnightApps.myapplication.app.core.Friendship;
import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.FriendshipDao;
import com.overnightApps.myapplication.app.dao.FriendshipRequestDao;
import com.overnightApps.myapplication.app.service.FriendshipSession;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by andre on 3/25/14.
 */
public class FriendshipSessionTest extends TestCase {
    @Mock
    FriendshipRequestDao friendshipRequestDaoStub;
    @Mock
    FriendshipDao friendshipDaoStub;
    private FriendshipSession friendshipSession;
    private User current;
    private User friend;

    public void setUp() {
        MockitoAnnotations.initMocks(this);
        current = new User("current@test.com","","",0,"","");
        friend = new User("from@test.com","","",0,"","");
        friendshipSession = new FriendshipSession(friendshipRequestDaoStub, friendshipDaoStub, current, friend);
    }

    public void test_getFriendshipState_noRequestSend_returnNoneState() throws Exception {
        when(friendshipRequestDaoStub.get(any(User.class), any(User.class))).thenReturn(null);

        Assert.assertEquals(FriendshipSession.FriendShipState.NONE,
                friendshipSession.getFriendshipState()
        );
    }

    public void test_getFriendshipState_CurrentUserSentRequest_returnWaitingResponseStatus() throws Exception {
        FriendShipRequest friendShipRequest = new FriendShipRequest(friend, current, false, false);
        when(friendshipRequestDaoStub.get(any(User.class), any(User.class))).thenReturn(friendShipRequest);

        Assert.assertEquals(FriendshipSession.FriendShipState.WAITING_FOR_RESPONSE,
                friendshipSession.getFriendshipState());
    }

    public void test_getFriendshipState_CurrentUserHasRequest_returnReceivedRequestStatus() throws Exception {
        FriendShipRequest friendShipRequest = new FriendShipRequest(current, friend, false, false);
        when(friendshipRequestDaoStub.get(any(User.class), any(User.class))).thenReturn(friendShipRequest);

        Assert.assertEquals(FriendshipSession.FriendShipState.RECEIVED_REQUEST,
                friendshipSession.getFriendshipState());
    }

    public void test_getFriendshipState_declinedFriendship_noneStatus() throws Exception {
        FriendShipRequest friendShipRequest = new FriendShipRequest(current, friend, false, true);
        when(friendshipRequestDaoStub.get(any(User.class), any(User.class))).thenReturn(friendShipRequest);

        Assert.assertEquals(FriendshipSession.FriendShipState.DENIED,
                friendshipSession.getFriendshipState());
    }

    public void test_getFriendshipState_untrustedFriend_untrustedStatus() throws Exception {
        FriendShipRequest friendShipRequest = new FriendShipRequest(current, friend, true, true);
        when(friendshipRequestDaoStub.get(any(User.class), any(User.class))).thenReturn(friendShipRequest);

        Friendship friendship = new Friendship(current, friend, false);
        when(friendshipDaoStub.getFriendship(any(User.class),any(User.class))).thenReturn(friendship);

        Assert.assertEquals(FriendshipSession.FriendShipState.UNTRUSTED_FRIEND,
                friendshipSession.getFriendshipState());
    }

    public void test_getFriendshipState_trustedFriend_trustedStatus() throws Exception {
        FriendShipRequest friendShipRequest = new FriendShipRequest(current, friend, true, true);
        when(friendshipRequestDaoStub.get(any(User.class), any(User.class))).thenReturn(friendShipRequest);

        Friendship friendship = new Friendship(current, friend, true);
        when(friendshipDaoStub.getFriendship(any(User.class),any(User.class))).thenReturn(friendship);

        Assert.assertEquals(FriendshipSession.FriendShipState.TRUSTED_FRIEND,
                friendshipSession.getFriendshipState());
    }

    public void test_isCurrentFriendTrusted_couldNotFindFriendship_returnsFalse() throws Exception {
        when(friendshipDaoStub.get(any(User.class),any(User.class))).thenReturn(null);
        Assert.assertEquals(friendshipSession.isFriendTrusted(),false);
    }

    public void test_isFriendTrusted_notTrustedFriendShip_returnsFalse() throws Exception {
        Friendship friendshipStub = mock(Friendship.class);
        when(friendshipStub.isTrusted()).thenReturn(false);

        when(friendshipDaoStub.getFriendship(any(User.class), any(User.class))).thenReturn(friendshipStub);

        Assert.assertFalse(friendshipSession.isFriendTrusted());
    }

    public void test_isFriendTrusted_trustedFriendship_returnsTrue() throws Exception {
        Friendship friendshipStub = mock(Friendship.class);
        when(friendshipStub.isTrusted()).thenReturn(true);

        when(friendshipDaoStub.getFriendship(any(User.class), any(User.class))).thenReturn(friendshipStub);

        Assert.assertTrue(friendshipSession.isFriendTrusted());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        friendshipRequestDaoStub = null;
        friendshipDaoStub = null;
        friendshipSession = null;
        current = null;
        friend = null;
    }
}
