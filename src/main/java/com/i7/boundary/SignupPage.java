package com.i7.boundary;

import com.i7.controller.SignupController;
import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/signup")
public class SignupPage {

    @Autowired
    private SignupController signupController;

    @GetMapping
    public String showSignupForm(Model model) {
        List<UserProfile> filteredProfiles = signupController.getFilteredProfiles();
        model.addAttribute("profiles", filteredProfiles);
        model.addAttribute("user", new UserAccount());
        return "signup";
    }

    @PostMapping
    public String handleSignup(HttpServletRequest request, Model model) {
        boolean success = signupController.processSignup(
            request.getParameter("firstName"),
            request.getParameter("lastName"),
            request.getParameter("email"),
            request.getParameter("uid"),
            request.getParameter("password"),
            request.getParameter("profile")
        );

        if (success) {
            model.addAttribute("message", "Account created successfully!");
            return "login";
        } else {
            model.addAttribute("error", "Account creation failed. Email or UID may already exist.");
            return "signup";
        }
    }
}
