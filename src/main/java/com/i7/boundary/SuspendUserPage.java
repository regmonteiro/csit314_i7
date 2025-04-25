package com.i7.boundary;

import com.i7.controller.UserManageController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin/suspendUser")
public class SuspendUserPage {

    @Autowired
    private UserManageController userManageController;

    @PostMapping
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