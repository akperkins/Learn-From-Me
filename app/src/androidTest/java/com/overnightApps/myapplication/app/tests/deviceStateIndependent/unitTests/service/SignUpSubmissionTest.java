package com.overnightApps.myapplication.app.tests.deviceStateIndependent.unitTests.service;

import android.graphics.Bitmap;

import com.overnightApps.myapplication.app.core.helper.SignUpForm;
import com.overnightApps.myapplication.app.dao.exceptions.UnableToSignUpException;
import com.overnightApps.myapplication.app.dao.UserDao;
import com.overnightApps.myapplication.app.service.SignUpSubmission;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by andre on 2/28/14.
 */
public class SignUpSubmissionTest extends TestCase {
    private SignUpSubmission signUpSubmission;
    @Mock private UserDao userDaoStub;

    private final String fullName = "jon snow";
    private final String email = "test@email.com";
    private final String password = "password";
    private final String publicSignature = "publicSignature";
    private final String privateSignature = "privateSignature";

    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(userDaoStub.isEmailAddressUsed(anyString())).thenReturn(false);
        when(userDaoStub.isPublicSignatureUsed(anyString())).thenReturn(false);
        when(userDaoStub.isPrivateSignatureUsed(anyString())).thenReturn(false);

        SignUpForm signUpForm = new SignUpForm(fullName,email,password,publicSignature,
                privateSignature);

        signUpSubmission = new SignUpSubmission(signUpForm, null, userDaoStub);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        signUpSubmission = null;
    }

    public void test_signUp_errorOnBackEndDuringSuccessfulSignUp_returnBackEndStatusCode() throws Exception {
        when(userDaoStub.signUserUp(any(SignUpForm.class),any(Bitmap.class))).thenThrow(new UnableToSignUpException());
        assertEquals(false, signUpSubmission.signUp());
    }

    public void test_signUp_successfulSubmission_returnSuccess() throws Exception {
        when(userDaoStub.signUserUp(any(SignUpForm.class),any(Bitmap.class))).thenReturn(true);
        assertEquals(true, signUpSubmission.signUp());
    }
}
