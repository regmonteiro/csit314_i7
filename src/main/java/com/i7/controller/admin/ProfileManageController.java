package com.i7.controller.admin;

import com.i7.entity.UserProfile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileManageController {

    public List<UserProfile> getProfiles() {
        return UserProfile.getAllProfiles();
    }

    public UserProfile getProfileDetails(String profileCode) {
        return UserProfile.findByCode(profileCode);
    }

    public boolean updateProfile(String profileCode, String name, String description) {
        UserProfile profile = UserProfile.findByCode(profileCode);
        
        if (profile != null) {
            return UserProfile.updateProfile(profileCode, name, description);
        }
        return false;
    }

    public boolean suspendProfile(String profileCode) {
        return UserProfile.updateProfileStatus(profileCode, "suspended");
    }
}
