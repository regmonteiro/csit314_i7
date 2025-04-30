package com.i7.controller.admin;

import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UpdateUserController {

    public UserAccount getAccountDetails(String uid) {
        return UserAccount.getUserAccount(uid);
    }

    public List<UserProfile> getAllProfiles() {
        return UserProfile.getAllProfiles();
    }

    public boolean updateUserDetails(String uid, UserAccount updatedDetails) {
        return UserAccount.saveUpdatedDetails(uid, updatedDetails);
    }

}
