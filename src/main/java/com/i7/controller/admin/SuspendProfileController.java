package com.i7.controller.admin;

import org.springframework.stereotype.Component;

import com.i7.entity.UserProfile;

@Component
public class SuspendProfileController {
        public boolean suspendProfile(String profileCode) {
        return UserProfile.updateProfileStatus(profileCode, "suspended");
    }
}
