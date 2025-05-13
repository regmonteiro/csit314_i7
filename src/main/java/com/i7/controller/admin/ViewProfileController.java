package com.i7.controller.admin;

import java.util.List;

import org.springframework.stereotype.Component;

import com.i7.entity.UserProfile;

@Component
public class ViewProfileController {
    public List<UserProfile> getProfiles() {
        return UserProfile.getAllProfiles();
    }

    public UserProfile getProfileDetails(String profileCode) {
        return UserProfile.findByCode(profileCode);
    }
}
