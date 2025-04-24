package com.i7.controller;

import com.i7.entity.UserAccount;
import org.springframework.stereotype.Component;

@Component
public class LoginController {

    public UserAccount validateLogin(String email, String password) {
        return UserAccount.authenticateLogin(email, password);
    }
}
