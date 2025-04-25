package com.i7.boundary;

import com.i7.controller.UserManageController;
import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserManagementPage {

    @Autowired
    private UserManageController userManageController;

    @GetMapping("/viewUserAccounts")
    public String showUserList(@RequestParam(required = false) String search, Model model) {
        List<UserAccount> userList;
        if (search != null && !search.trim().isEmpty()) {
            userList = userManageController.searchUserAccounts(search.trim());
            if (userList.isEmpty()) {
                model.addAttribute("error", "No users found.");
            }
        } else {
            userList = userManageController.getAllUserAccounts();
        }
        model.addAttribute("users", userList);
        model.addAttribute("search", search);
        return "viewUserAccounts";
    }

    @GetMapping("/viewUser")
    public String showSingleUser(@RequestParam("uid") String uid,
                                 @RequestParam(value = "message", required = false) String message,
                                 Model model) {
        UserAccount user = userManageController.getUserByUid(uid);
        if (user != null) {
            UserProfile profile = userManageController.getProfileByCode(user.getProfileCode());
            model.addAttribute("user", user);
            model.addAttribute("profile", profile);
            if (message != null) model.addAttribute("message", message);
            return "viewUser";
        } else {
            model.addAttribute("error", "User not found.");
            return "userError";
        }
    }

    @GetMapping("/updateUser")
    public String showUpdateForm(@RequestParam("uid") String uid, Model model) {
        UserAccount user = userManageController.getUserByUid(uid);
        model.addAttribute("user", user);
        model.addAttribute("profiles", userManageController.getAllProfiles());
        return "updateUser";
    }

    @PostMapping("/updateUser")
    public String handleUpdate(@ModelAttribute("user") UserAccount updatedDetails, RedirectAttributes redirectAttributes) {
        boolean success = userManageController.updateUserDetails(updatedDetails.getUid(), updatedDetails);

        if (!success) {
            redirectAttributes.addFlashAttribute("error", "User not found or update failed.");
            return "redirect:/admin/updateUser?uid=" + updatedDetails.getUid();
        }

        redirectAttributes.addFlashAttribute("message", "User updated successfully.");
        return "redirect:/admin/viewUser?uid=" + updatedDetails.getUid();
    }

    @PostMapping("/suspendUser")
    public String suspendUser(@RequestParam("uid") String uid, RedirectAttributes redirectAttributes) {
        boolean success = userManageController.suspendUser(uid);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "User suspended successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "User is already suspended or an error occurred.");
        }
        return "redirect:/admin/viewUser?uid=" + uid;
    }
}
