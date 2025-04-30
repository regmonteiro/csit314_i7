package com.i7.boundary.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.i7.controller.admin.SuspendProfileController;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class SuspendProfilePage {
    @Autowired
    SuspendProfileController suspendProfileController;

    @PostMapping("/suspendProfile")
    public String suspendProfile(@RequestParam("profileCode") String profileCode, RedirectAttributes redirectAttributes, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }
    
        boolean success = suspendProfileController.suspendProfile(profileCode); 

        if (success) {
            redirectAttributes.addFlashAttribute("message", "Profile suspended successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to suspend profile.");
        }
    
        return "redirect:/admin/viewUserProfile?profileCode=" + profileCode;
    }
}
