package com.i7.controller;

import com.i7.entity.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/createUser")
public class UserCreateController {

    @GetMapping
    public String showCreateForm() {
        return "createUserAccount";
    }

    @PostMapping
    public String processCreateUser(@RequestParam String firstName,
                                    @RequestParam String lastName,
                                    @RequestParam String email,
                                    @RequestParam String username,
                                    @RequestParam String password,
                                    @RequestParam String role,
                                    Model model) {

        boolean created = UserAccount.createNewAccount(firstName, lastName, email, username, password, role);
        if (created) {
            model.addAttribute("success", "User created successfully.");
        } else {
            model.addAttribute("error", "This email is already associated with an account.");
        }

        return "createUserAccount";
    }
}
