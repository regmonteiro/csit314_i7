package com.i7.controller;

import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserManageController {

    @GetMapping("/viewUser")
    public String viewUserAccount(@RequestParam("uid") String uid, Model model) {
        UserAccount user = UserAccount.getUserAccount(uid);

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
    public String showUpdateForm(@RequestParam("uid") String uid, Model model) {
        UserAccount user = UserAccount.getUserAccount(uid);
        List<UserProfile> profiles = UserProfile.getAllProfiles();

        model.addAttribute("user", user);
        model.addAttribute("profiles", profiles);
        return "updateUser";
    }

    @PostMapping("/updateUser")
    public String updateDetails(@ModelAttribute UserAccount updatedDetails, Model model) {
        if (!UserAccount.validateDetails(updatedDetails)) {
            model.addAttribute("error", "Please fill in all required fields.");
            model.addAttribute("user", updatedDetails);
            model.addAttribute("profiles", UserProfile.getAllProfiles());
            return "updateUser";
        }

        boolean success = UserAccount.saveUpdatedDetails(updatedDetails.getUid(), updatedDetails);
        if (success) {
            model.addAttribute("message", "User updated successfully.");
            return "redirect:/admin/viewUserAccounts";
        } else {
            model.addAttribute("error", "User not found or update failed.");
            model.addAttribute("user", updatedDetails);
            model.addAttribute("profiles", UserProfile.getAllProfiles());
            return "updateUser";
        }
    }

    @PostMapping("/suspendUser")
    public String suspendUserAccount(@RequestParam("uid") String uid, Model model) {
        boolean success = UserAccount.updateAccountStatus(uid, "suspended");

        if (success) {
            model.addAttribute("message", "User suspended successfully.");
        } else {
            model.addAttribute("error", "User is already suspended or action cancelled.");
        }

        return "redirect:/admin/viewUser?uid=" + uid;
    }
}
