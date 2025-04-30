package com.i7.boundary.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.i7.controller.admin.UpdateProfileController;
import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;
import com.i7.utility.SessionHelper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class UpdateProfilePage {
    
    @Autowired
    private UpdateProfileController updateProfileController;

    @GetMapping("/updateProfile")
    public String updateProfileForm(@RequestParam("profileCode") String profileCode, Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login"; // Redirect if no user is logged in
        }

        session.setAttribute("tab", "profiles");
        model.addAttribute("user", sessionUser); 
        model.addAttribute("activePage", "viewProfiles");

        UserProfile profile = updateProfileController.getProfileDetails(profileCode);
        model.addAttribute("profile", profile);

        return "admin/updateProfile"; 
    }

    @PostMapping("/updateProfile")
    public String handleUpdate(@ModelAttribute UserProfile profile, RedirectAttributes redirectAttributes) {
        boolean success = updateProfileController.updateProfile(profile.getCode(), profile.getName(), profile.getDescription());
    
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Profile updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile.");
        }
    
        return "redirect:/admin/viewUserProfile?profileCode=" + profile.getCode();
    }
}
