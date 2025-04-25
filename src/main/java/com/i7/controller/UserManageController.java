package com.i7.controller;

import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserManageController {

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

    public boolean updateUserDetails(String uid, UserAccount updatedDetails) {
        return UserAccount.saveUpdatedDetails(uid, updatedDetails);
    }

    public boolean suspendAccount(String uid) {
        return UserAccount.updateAccountStatus(uid, "suspended");
    }

    public List<UserAccount> searchUserAccounts(String uid) {
        return UserAccount.searchByQuery(uid);
    }
    
}
