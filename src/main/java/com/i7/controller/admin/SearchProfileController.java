package com.i7.controller.admin;

import java.util.List;

import org.springframework.stereotype.Component;

import com.i7.entity.UserProfile;

@Component
public class SearchProfileController {
        public List<UserProfile> searchUserProfiles(String searchQuery) {
        return UserProfile.findProfilesByQuery(searchQuery);
    }
}
