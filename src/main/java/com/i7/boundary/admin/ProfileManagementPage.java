package com.i7.boundary.admin;

import com.i7.controller.admin.ProfileManageController;
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
public class ProfileManagementPage {

    @Autowired
    private ProfileManageController profileManageController;

    @GetMapping("/viewProfiles")
    public String showProfiles(Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            model.addAttribute("error", "No user logged in");
            return "redirect:/login";  // Redirect to login if no user is logged in
        }
    
        
        session.setAttribute("tab", "profiles");
        model.addAttribute("user", sessionUser); 
        model.addAttribute("activePage", "viewProfiles");
    
        List<UserProfile> userProfiles = profileManageController.getProfiles();
        model.addAttribute("userProfiles", userProfiles);
    
        return "admin/viewProfiles";
    }
    
    @GetMapping("/viewUserProfile")
    public String viewProfileDetails(@RequestParam("profileCode") String profileCode, Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login"; 
        }
    
        session.setAttribute("tab", "profiles");
        model.addAttribute("user", sessionUser); 
        model.addAttribute("activePage", "viewProfiles");
    
        UserProfile profile = profileManageController.getProfileDetails(profileCode);
        model.addAttribute("profile", profile);
    
        return "admin/viewUserProfileDetails";
    }
    

    @GetMapping("/updateProfile")
    public String updateProfileForm(@RequestParam("profileCode") String profileCode, Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login"; // Redirect if no user is logged in
        }

        session.setAttribute("tab", "profiles");
        model.addAttribute("user", sessionUser); 
        model.addAttribute("activePage", "viewProfiles");

        UserProfile profile = profileManageController.getProfileDetails(profileCode);
        model.addAttribute("profile", profile);

        return "admin/updateProfile"; 
    }

    @PostMapping("/updateProfile")
    public String handleUpdate(@ModelAttribute UserProfile profile, Model model) {
        boolean success = profileManageController.updateProfile(profile.getCode(), profile.getName(), profile.getDescription()); 

        if (success) {
            model.addAttribute("message", "Profile updated successfully.");
        } else {
            model.addAttribute("error", "Failed to update profile.");
        }

        return "redirect:/admin/viewUserProfile?profileCode=" + profile.getCode();
    }

    @PostMapping("/suspendProfile")
    public String suspendProfile(@RequestParam("profileCode") String profileCode, RedirectAttributes redirectAttributes, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }
    
        boolean success = profileManageController.suspendProfile(profileCode); 

        if (success) {
            redirectAttributes.addFlashAttribute("message", "Profile suspended successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to suspend profile.");
        }
    
        return "redirect:/admin/viewProfiles";
    }
}