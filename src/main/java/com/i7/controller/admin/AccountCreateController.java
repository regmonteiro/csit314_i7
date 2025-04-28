package com.i7.controller.admin;

import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AccountCreateController {

    public List<UserProfile> getAllProfiles() {
        return UserProfile.getAllProfiles();
    }

    public List<String> getAllowedSignupProfiles() {
        return UserProfile.getAllowedProfilesForSignup();
    }

    public UserAccount createAccount(String firstName, String lastName, String email, String password, String profileCode) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
    
        boolean created = UserAccount.createUserAccount(
            firstName, 
            lastName, 
            email, 
            password, 
            profileCode,
            "active"
        );
    
        if (!created) {
            return null;
        }
    
        return UserAccount.findByEmail(email);
    }
    
}
