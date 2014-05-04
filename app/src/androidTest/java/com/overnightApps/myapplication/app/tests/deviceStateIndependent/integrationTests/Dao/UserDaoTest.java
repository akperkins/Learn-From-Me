package com.overnightApps.myapplication.app.tests.deviceStateIndependent.integrationTests.Dao;

import com.overnightApps.myapplication.app.dao.UserDao;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by andre on 4/7/14.
 */
public class UserDaoTest extends TestCase {
    private UserDao userDao;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        userDao = UserDao.instance();
    }

    public void test_isEmailAddressUsed_usedEmail_returnTrue() throws Exception {
        final String ALREADY_USED_EMAIL = "test@test.com";
        Assert.assertTrue(userDao.isEmailAddressUsed(ALREADY_USED_EMAIL));
    }

    public void test_isEmailAddressUsed_notUsedEmail_returnFalse() throws Exception {
        final String UNUSED_EMAIL = "askjfdsjafidshfakd@gmail.com";
        Assert.assertFalse(userDao.isEmailAddressUsed(UNUSED_EMAIL));
    }

    public void test_isPublicSignatureUsed_usedSignature_returnsTrue() throws Exception {
        final String USED_SIGNATURE = "Andre";
        Assert.assertTrue(userDao.isPublicSignatureUsed(USED_SIGNATURE));
    }

    public void test_isPublicSignatureUsed_unusedSignature_returnsFalse() throws Exception {
        final String UNUSED_SIGNATURE = "PEJINSAKJFEIUWEFS";
        Assert.assertFalse(userDao.isPublicSignatureUsed( UNUSED_SIGNATURE));
    }

    public void test_isPrivateSignatureUsed_usedSignature_returnsTrue() throws Exception {
        final String USED_SIGNATURE = "privateAndre";
        Assert.assertTrue(userDao.isPrivateSignatureUsed(USED_SIGNATURE));
    }

    public void test_isPrivateSignatureUsed_unusedSignature_returnsFalse() throws Exception {
        final String UNUSED_SIGNATURE = "privatePEJINSAKJFEIUWEFS";
        Assert.assertFalse(userDao.isPrivateSignatureUsed( UNUSED_SIGNATURE));
    }
}