package com.i7.controller;

import com.i7.entity.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Boundary (UI)
    }

    @PostMapping("/login")
    public String validateLogin(@RequestParam String email,
                                @RequestParam String password,
                                Model model) {

        UserAccount user = UserAccount.authenticateLogin(email, password); // Entity call

        if (user == null) {
            model.addAttribute("error", "Invalid credentials or suspended account.");
            return "login";
        }

        String profileName = user.getProfile().getName();
        model.addAttribute("profile", profileName);

        if (profileName.equalsIgnoreCase("user admin") || profileName.equalsIgnoreCase("admin")) {
            return "adminDashboard";
        } else {
            return "dashboard";
        }
    }
}