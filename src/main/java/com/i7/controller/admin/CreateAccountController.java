package com.i7.controller.admin;

import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CreateAccountController {

    public List<UserProfile> getAllProfiles() {
        return UserProfile.getAllProfiles();
    }
    
    public UserAccount createAccount(String firstName, String lastName, String email, String password, String profileCode) {
        String status = "active"; 
        boolean success = UserAccount.createUserAccount(firstName, lastName, email, password, profileCode, status);

        if (success) {
            return UserAccount.findByEmail(email);
        } else {
            return null;
        }
    }
}
