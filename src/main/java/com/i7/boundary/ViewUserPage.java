package com.i7.boundary;

import com.i7.controller.UserManageController;
import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;

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
    public String showUserList(Model model) {
        List<UserAccount> userList = userManageController.getAllUserAccounts();
        model.addAttribute("users", userList);
        return "viewUserAccounts";
    }

   @GetMapping("/viewUser")
    public String showSingleUser(@RequestParam("uid") String uid, Model model) {
        UserAccount user = userManageController.getUserByUid(uid);
        if (user != null) {
            UserProfile profile = UserProfile.findByCode(user.getProfileCode());
            model.addAttribute("user", user);
            model.addAttribute("profile", profile);
            return "viewUser";
        } else {
            model.addAttribute("error", "User not found.");
            return "userError";
        }
    }
}
