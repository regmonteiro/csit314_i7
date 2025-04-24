package com.i7.controller;

import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SignupController {

    public boolean processSignup(String firstName, String lastName, String email,
                                     String uid, String password, String profileCode) {
        String status = "active";

        boolean success = UserAccount.createNewAccount(firstName, lastName, email, uid, password, profileCode, status);

        return success;
    }

    public List<UserProfile> getFilteredProfiles() {
        return UserProfile.getAllProfiles().stream()
                .filter(p -> {
                    String code = p.getCode().toLowerCase();
                    return code.equals("homeowner") || code.equals("cleaner");
                })
                .collect(Collectors.toList());
    }
}
