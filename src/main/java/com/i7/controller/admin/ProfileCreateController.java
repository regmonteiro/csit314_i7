package com.i7.controller.admin;

import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ProfileCreateController {

    public List<UserProfile> getAllProfiles() {
        return UserProfile.getAllProfiles();
    }

    public List<String> getAllowedSignupProfiles() {
        return UserProfile.getAllowedProfilesForSignup();
    }

    public UserProfile createProfile(String code, String name, String description) {
        boolean success = UserProfile.createProfile(code, name, description);
        if (success) {
            return UserProfile.findByCode(code);
        } else {
            return null;
        }
    }
}


