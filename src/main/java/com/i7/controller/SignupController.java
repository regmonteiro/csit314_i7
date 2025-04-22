package com.i7.controller;

import com.i7.entity.UserAccount;
import com.i7.entity.Role;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class SignupController {

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        List<Role> allRoles = Role.getAllRoles();

        // Filter roles: only homeowner and cleaner
        List<Role> allowedRoles = new ArrayList<>();
        for (Role role : allRoles) {
            String code = role.getCode().toLowerCase();
            if (code.equals("homeowner") || code.equals("cleaner")) {
                allowedRoles.add(role);
            }
        }

        model.addAttribute("roles", allowedRoles);
        model.addAttribute("user", new UserAccount());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String email,
                                @RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String role,
                                Model model) {
        String status = "active";

        // Call Entity to insert user
        boolean success = UserAccount.createNewAccount(firstName, lastName, email, username, password, role, status);
        
        if (success) {
            model.addAttribute("message", "Account created successfully!");
            return "login";
        } else {
            model.addAttribute("error", "Account creation failed. Email or username may already exist.");
            return "signup";
        }
    }
}