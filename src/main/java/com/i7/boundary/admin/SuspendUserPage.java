package com.i7.boundary.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.i7.controller.admin.SuspendUserController;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class SuspendUserPage {
    @Autowired
    private SuspendUserController suspendUserController;

    @PostMapping("/suspendUser")
    public String suspendUser(@RequestParam("uid") String uid,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }

        boolean success = suspendUserController.suspendAccount(uid);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "User suspended successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "User is already suspended or an error occurred.");
        }
        return "redirect:/admin/viewUser?uid=" + uid;
    }
}
