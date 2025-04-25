package com.i7.controller;

import com.i7.entity.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        boolean success = UserAccount.logout(session);

        if (success) {
            return "redirect:/login";
        } else {
            return "error";
        }
    }
}
