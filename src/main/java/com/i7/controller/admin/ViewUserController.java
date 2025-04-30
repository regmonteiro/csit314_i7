package com.i7.controller.admin;

import java.util.List;

import org.springframework.stereotype.Component;

import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;

@Component
public class ViewUserController {
    public List<UserAccount> getAllUserAccounts() {
    return UserAccount.getAllUserAccounts();
    }

    public List<UserProfile> getAllProfiles() {
        return UserProfile.getAllProfiles();
    }

    public UserAccount getAccountDetails(String uid) {
        return UserAccount.getUserAccount(uid);
    }

    public UserProfile getProfileByCode(String code) {
        return UserProfile.findByCode(code);
    }
}
