package com.i7.controller;

import com.i7.entity.UserAccount;
import com.i7.entity.Role;

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

    @GetMapping("/updateUser")
    public String showUpdateForm(@RequestParam("username") String username, Model model) {
        UserAccount user = UserAccount.getUserAccount(username);
        List<Role> roles = Role.getAllRoles();
    
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "updateUser";
    }

    @PostMapping("/updateUser")
    public String updateDetails(@ModelAttribute UserAccount updatedDetails, Model model) {
        if (!UserAccount.validateDetails(updatedDetails)) {
            model.addAttribute("error", "Please fill in all required fields.");
            model.addAttribute("user", updatedDetails);
            model.addAttribute("roles", Role.getAllRoles());
            return "updateUser";
        }
    
        boolean success = UserAccount.saveUpdatedDetails(updatedDetails.getUsername(), updatedDetails);
        if (success) {
            model.addAttribute("message", "User updated successfully.");
            return "redirect:/admin/viewUserAccounts";
        } else {
            model.addAttribute("error", "User not found or update failed.");
            model.addAttribute("user", updatedDetails);
            model.addAttribute("roles", Role.getAllRoles());
            return "updateUser";
        }
    }
    
    @PostMapping("/suspendUser")
    public String suspendUserAccount(@RequestParam("userId") String userId, Model model) {
        boolean success = UserAccount.updateAccountStatus(userId, "suspended");

        if (success) {
            model.addAttribute("message", "User suspended successfully.");
        } else {
            model.addAttribute("error", "User is already suspended or action cancelled.");
        }

        return "redirect:/admin/viewUser?username=" + userId;
    }
}
