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
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {

        UserAccount user = UserAccount.findByEmail(email); // Entity access

        if (user != null && user.checkPassword(password)) {
            model.addAttribute("role", user.getRole());
            return "dashboard"; // Boundary
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }
}