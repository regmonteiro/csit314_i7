package com.i7.controller;

import com.i7.entity.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class SignupController {

    @GetMapping("/signup")
    public String showSignupForm() {
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
        // Call Entity to insert user
        boolean success = UserAccount.createNewAccount(firstName, lastName, email, username, password, role);

        if (success) {
            model.addAttribute("message", "Account created successfully!");
            return "login";
        } else {
            model.addAttribute("error", "Account creation failed. Email or username may already exist.");
            return "signup";
        }
    }
}