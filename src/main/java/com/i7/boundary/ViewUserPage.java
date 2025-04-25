package com.i7.boundary;

import com.i7.controller.UserManageController;
import com.i7.entity.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ViewUserPage {

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
    public String showSingleUser(@RequestParam("uid") String uid, Model model) {
        UserAccount user = userManageController.getUserByUid(uid);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("profile", userManageController.getProfileByCode(user.getProfileCode()));
            return "viewUser";
        } else {
            model.addAttribute("error", "User not found.");
            return "userError";
        }
    }
}
