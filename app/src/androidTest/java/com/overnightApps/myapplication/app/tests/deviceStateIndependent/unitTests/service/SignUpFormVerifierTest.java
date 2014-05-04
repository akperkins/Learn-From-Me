package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.service;

import com.overnightApps.myapplication.app.core.helper.SignUpForm;
import com.overnightApps.myapplication.app.dao.UserDao;
import com.overnightApps.myapplication.app.service.SignUpFormVerifier;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by andre on 4/25/14.
 */
public class SignUpFormVerifierTest extends TestCase {
    private SignUpFormVerifier signUpFormVerifier;

    @Mock
    UserDao userDaoStub;

    private final String fullName = "jon snow";
    private final String email = "test@email.com";
    private final String password = "password";
    private final String passwordConfirm = "password";
    private final String publicSignature = "publicSignature";
    private final String privateSignature = "privateSignature";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        signUpFormVerifier = new SignUpFormVerifier(
                new SignUpForm(fullName, email, password,  publicSignature, privateSignature), userDaoStub);

    }

    /** I would parameterize this but can not due so with junit3*/
    public void test_performClientSideChecks_anyFieldsEmptyFailError_error() throws Exception {
        signUpFormVerifier = new SignUpFormVerifier(
                new SignUpForm(fullName, "", password,  publicSignature, privateSignature), userDaoStub);

        signUpFormVerifier.getSignUpForm();
        assertEquals(SignUpFormVerifier.StatusCode.REQUIRED_FIELDS_EMPTY,
                signUpFormVerifier.performClientSideChecks());
    }

    public void test_performClientSideChecks_passwordIsLessThan6Characters_error() {
        signUpFormVerifier = new SignUpFormVerifier(
                new SignUpForm(fullName, email, "passe", publicSignature, privateSignature), userDaoStub);;
        assertEquals(SignUpFormVerifier.StatusCode.PASSWORD_TOO_SHORT,
                signUpFormVerifier.performClientSideChecks());
    }

    public void test_performClientSideChecks_alreadyUsedEmail_returnError(){
        UserDao dao = mock(UserDao.class);
        when(dao.isEmailAddressUsed(anyString())).thenReturn(true);
        signUpFormVerifier.setDao(dao);
        assertEquals(SignUpFormVerifier.StatusCode.EMAIL_ALREADY_USED,
                signUpFormVerifier.performClientSideChecks());
    }

    public void test_performClientSideChecks_alreadyUsedPublicSignature_returnError(){
        UserDao dao = mock(UserDao.class);
        when(dao.isPublicSignatureUsed(matches(publicSignature))).thenReturn(true);
        signUpFormVerifier.setDao(dao);
        assertEquals(SignUpFormVerifier.StatusCode.PUBLIC_SIGNATURE_USED,
                signUpFormVerifier.performClientSideChecks());
    }

    public void test_performClientSideChecks_alreadyUsedPrivateSignature_returnError(){
        UserDao dao = mock(UserDao.class);
        when(dao.isPrivateSignatureUsed(matches(privateSignature))).thenReturn(true);
        signUpFormVerifier.setDao(dao);
        assertEquals(SignUpFormVerifier.StatusCode.PRIVATE_SIGNATURE_USED,
                signUpFormVerifier.performClientSideChecks());
    }

    public void test_performClientSideChecks_userEnterValidData_returnChecksPassed(){
        assertEquals(SignUpFormVerifier.StatusCode.PASSED_INITIAL_CHECKS,
                signUpFormVerifier.performClientSideChecks());
    }
}
