package com.i7.controller.admin;

import org.springframework.stereotype.Component;

import com.i7.entity.UserProfile;

@Component
public class UpdateProfileController {
    public boolean updateProfile(String profileCode, String name, String description) {
        UserProfile profile = UserProfile.findByCode(profileCode);
        if (profile != null) {
            return UserProfile.updateProfile(profileCode, name, description);
        }
        return false;
    }

    public UserProfile getProfileDetails(String profileCode) {
        return UserProfile.findByCode(profileCode);
    }
}
