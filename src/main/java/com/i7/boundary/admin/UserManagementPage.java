package com.i7.boundary.admin;

import com.i7.controller.admin.UserManageController;
import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;
import com.i7.utility.SessionHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserManagementPage {

    @Autowired
    private UserManageController userManageController;

    @GetMapping("/viewUserAccounts")
    public String showUserList(@RequestParam(required = false) String search, Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", sessionUser);
        model.addAttribute("activePage", "viewUsers");
        model.addAttribute("tab", "accounts");

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
        return "admin/viewUserAccounts";
    }

    @GetMapping("/viewUser")
    public String showSingleUser(@RequestParam("uid") String uid,
                                @RequestParam(value = "message", required = false) String message,
                                Model model,
                                HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", sessionUser);
        model.addAttribute("activePage", "viewUsers");
        model.addAttribute("tab", "accounts");

        UserAccount user = userManageController.getAccountDetails(uid);
        if (user != null) {
            UserProfile profile = userManageController.getProfileByCode(user.getProfileCode());
            model.addAttribute("viewedUser", user);
            model.addAttribute("profile", profile);
            if (message != null) model.addAttribute("message", message);
            return "admin/viewUser";
        } else {
            model.addAttribute("error", "User not found.");
            return "userError";
        }
    }

    @GetMapping("/updateUser")
    public String showUpdateForm(@RequestParam("uid") String uid, Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", sessionUser);
        model.addAttribute("activePage", "viewUsers");
        model.addAttribute("tab", "accounts");

        UserAccount user = userManageController.getAccountDetails(uid);
        model.addAttribute("userToUpdate", user);
        model.addAttribute("profiles", userManageController.getAllProfiles());
        return "admin/updateUser";
    }

    @PostMapping("/suspendUser")
    public String suspendUser(@RequestParam("uid") String uid,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }

        boolean success = userManageController.suspendAccount(uid);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "User suspended successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "User is already suspended or an error occurred.");
        }
        return "redirect:/admin/viewUser?uid=" + uid;
    }
}
