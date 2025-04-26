package com.i7.boundary.admin;

import com.i7.controller.admin.AccountCreateController;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/createUser")
public class CreateAccountPage {

    @Autowired
    private AccountCreateController accountCreateController;

    @GetMapping
    public String showCreateForm(Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", sessionUser);
        model.addAttribute("activePage", "createUser");
        model.addAttribute("newUser", new UserAccount());
        model.addAttribute("profiles", accountCreateController.getAllProfiles());

        return "admin/createUserAccount";
    }

    @PostMapping
    public String handleCreate(@ModelAttribute("newUser") UserAccount newUser, Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }

        UserAccount acc = accountCreateController.createAccount(
                newUser.getFirstName(), newUser.getLastName(), newUser.getEmail(), newUser.getPassword(), newUser.getProfileCode()
        );

        if (acc != null) {
            model.addAttribute("success", "User account created successfully.");
            model.addAttribute("newUser", new UserAccount());
        } else {
            model.addAttribute("error", "Account creation failed. Email might already exist.");
        }

        model.addAttribute("user", sessionUser);
        model.addAttribute("activePage", "createUser");
        model.addAttribute("profiles", accountCreateController.getAllProfiles());

        return "admin/createUserAccount";
    }
}
