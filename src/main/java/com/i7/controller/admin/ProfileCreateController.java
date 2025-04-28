package com.i7.controller.admin;

import com.i7.entity.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class ProfileCreateController {
    public boolean createProfile(String name, String description) {
        if (name == null || name.trim().isEmpty()) return false;
        if (description == null || description.trim().isEmpty()) return false;
        
        return UserProfile.createProfile(name, description);
    }
}

