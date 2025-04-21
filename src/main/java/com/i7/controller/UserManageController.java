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

    @GetMapping("/updateUser")
    public String showUpdateUserForm(@RequestParam("username") String username, Model model) {
        UserAccount user = UserAccount.getUserAccount(username);
        if (user != null) {
            model.addAttribute("user", user);
            return "updateUser"; // Must match the HTML file name exactly
        } else {
            model.addAttribute("error", "User not found.");
            return "userError";
        }
    }
    

    @PostMapping("/updateUser")
        public String updateDetails(@RequestParam String userId,
                                @ModelAttribute UserAccount updatedDetails,
                                Model model) {
        boolean isValid = UserAccount.validateDetails(updatedDetails);

        if (!isValid) {
            model.addAttribute("error", "Please fill in all required fields.");
            model.addAttribute("user", updatedDetails);
            return "updateUser";
        }

        boolean updated = UserAccount.saveUpdatedDetails(userId, updatedDetails);
        if (updated) {
            model.addAttribute("message", "User updated successfully.");
            return "redirect:/admin/viewUserAccounts";
        } else {
            model.addAttribute("error", "User not found or update failed.");
            model.addAttribute("user", updatedDetails);
            return "updateUser";
        }
    }
}
