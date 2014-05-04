package com.overnightApps.myapplication.app.service;

import com.overnightApps.myapplication.app.core.User;
import com.overnightApps.myapplication.app.dao.UserDao;

/**
 * Created by andre on 3/28/14.
 */
public class UserLogIn {
    private final UserDao userDao;
    private final String email;
    private final String password;

    public UserLogIn(String email, String password, UserDao userDao) {
        this.userDao = userDao;
        this.email = email;
        this.password = password;
    }

    public StatusCode logUserIn() {
        User user = null;
        user = userDao.logIn(email, password);
        if (user != null) {
            return StatusCode.SUCCESS;
        } else {
            return StatusCode.FAILURE;
        }
    }

    public enum StatusCode {
        SUCCESS("Successful login attempt"), FAILURE("Invalid Username/Password");
        private String message;

        StatusCode(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void appendMessage(String additionalInfo) {
            this.message += additionalInfo;
        }
    }
}
