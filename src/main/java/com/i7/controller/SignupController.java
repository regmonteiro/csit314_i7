package com.i7.controller;

import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class SignupController {

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        List<UserProfile> allProfiles = UserProfile.getAllProfiles();

        // Filter profiles: only homeowner and cleaner
        List<UserProfile> allowedProfiles = new ArrayList<>();
        for (UserProfile profile : allProfiles) {
            String code = profile.getCode().toLowerCase();
            if (code.equals("homeowner") || code.equals("cleaner")) {
                allowedProfiles.add(profile);
            }
        }

        model.addAttribute("profiles", allowedProfiles);
        model.addAttribute("user", new UserAccount());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String email,
                                @RequestParam String uid,
                                @RequestParam String password,
                                @RequestParam String profile,
                                Model model) {
        String status = "active";

        // Call Entity to insert user
        boolean success = UserAccount.createNewAccount(firstName, lastName, email, uid, password, profile, status);

        if (success) {
            model.addAttribute("message", "Account created successfully!");
            return "login";
        } else {
            model.addAttribute("error", "Account creation failed. Email or UID may already exist.");
            return "signup";
        }
    }
}
