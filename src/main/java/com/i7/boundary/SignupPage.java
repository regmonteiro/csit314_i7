package com.i7.boundary;

import com.i7.controller.AccountCreateController;
import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/signup")
public class SignupPage {

    @Autowired
    private AccountCreateController accountCreateController;

    @GetMapping
    public String showSignupForm(Model model) {
        List<UserProfile> allProfiles = accountCreateController.getAllProfiles();
        List<String> allowedCodes = accountCreateController.getAllowedSignupProfiles();

        List<UserProfile> filteredProfiles = allProfiles.stream()
            .filter(p -> allowedCodes.contains(p.getCode().toLowerCase()))
            .collect(Collectors.toList());

        model.addAttribute("profiles", filteredProfiles);
        model.addAttribute("user", new UserAccount());
        return "signup";
    }

    @PostMapping
    public String handleSignup(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String profileCode,
                           Model model) {

    UserAccount acc = accountCreateController.createAccount(firstName, lastName, email, password, profileCode);
    
    if (acc != null) {
        model.addAttribute("message", "Account created successfully!");
        return "login";
    } else {
        model.addAttribute("error", "Signup failed.");
        return "signup";
    }
}

}
