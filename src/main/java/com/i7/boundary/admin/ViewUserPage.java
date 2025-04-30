package com.i7.boundary.admin;

import com.i7.controller.admin.SearchUserController;
import com.i7.controller.admin.ViewUserController;
import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;
import com.i7.utility.SessionHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ViewUserPage {

    @Autowired
    private ViewUserController viewUserController;
    private SearchUserController searchUserController;

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
            userList = searchUserController.searchUserAccounts(search.trim());
            if (userList.isEmpty()) {
                model.addAttribute("error", "No users found.");
            }
        } else {
            userList = viewUserController.getAllUserAccounts();
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

        UserAccount user = viewUserController.getAccountDetails(uid);
        if (user != null) {
            UserProfile profile = viewUserController.getProfileByCode(user.getProfileCode());
            model.addAttribute("viewedUser", user);
            model.addAttribute("profile", profile);
            if (message != null) model.addAttribute("message", message);
            return "admin/viewUser";
        } else {
            model.addAttribute("error", "User not found.");
            return "userError";
        }
    }
}