package com.i7.boundary;

import com.i7.controller.AccountCreateController;
import com.i7.entity.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/createUser")
public class CreateAccountPage {

    @Autowired
    private AccountCreateController accountCreateController;

    @GetMapping
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserAccount());
        model.addAttribute("profiles", accountCreateController.getAllProfiles());
        return "createUserAccount";
    }

    @PostMapping
    public String handleCreate(@ModelAttribute("user") UserAccount user, Model model) {
        UserAccount acc = accountCreateController.createAccount(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getProfileCode());
        if (acc != null) {
            model.addAttribute("success", "User account created successfully.");
            model.addAttribute("user", new UserAccount());
        } else {
            model.addAttribute("error", "Account creation failed. Email might already exist.");
        }
        model.addAttribute("profiles", accountCreateController.getAllProfiles());
        return "createUserAccount";
    }
}

