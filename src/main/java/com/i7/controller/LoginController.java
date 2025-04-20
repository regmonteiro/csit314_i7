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
    public String validateLogin(@RequestParam String username,
                                @RequestParam String password,
                                Model model) {

        UserAccount user = UserAccount.authenticateLogin(username, password); // Entity call

        if (user == null) {
            model.addAttribute("error", "Invalid credentials or suspended account.");
            return "login";
        }

        model.addAttribute("role", user.getRole());

        if (user.getRole().equalsIgnoreCase("user admin") || user.getRole().equalsIgnoreCase("admin")) {
            return "adminDashboard";
        } else {
            return "dashboard";
        }
    }
}
