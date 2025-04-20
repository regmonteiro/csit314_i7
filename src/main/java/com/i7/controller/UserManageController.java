package com.i7.controller;

import com.i7.entity.UserAccount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserManageController {

    @GetMapping("/viewUser")
    public String viewUserAccount(@RequestParam("username") String username, Model model) {
        UserAccount user = UserAccount.getUserAccount(username);
        
        if (user != null) {
            model.addAttribute("user", user);
            return "viewUser";
        } else {
            model.addAttribute("error", "User not found.");
            return "userError";
        }
    }

    @GetMapping("/viewUserAccounts")
    public String viewAllUserAccounts(Model model) {
    List<UserAccount> userList = UserAccount.getAllUserAccounts();
    model.addAttribute("users", userList);
    return "viewUserAccounts";
}

}
