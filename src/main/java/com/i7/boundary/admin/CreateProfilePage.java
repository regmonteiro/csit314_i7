package com.i7.boundary.admin;

import com.i7.controller.admin.CreateProfileController;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/createProfile")
public class CreateProfilePage {

    @Autowired
    private CreateProfileController profileCreateController;

    @GetMapping
    public String showCreateProfileForm(Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", sessionUser);
        model.addAttribute("activePage", "createProfile");
        model.addAttribute("tab", "profiles");
        model.addAttribute("newProfile", new com.i7.entity.UserProfile());

        return "admin/createUserProfile";
    }

    @PostMapping
    public String handleCreateProfile(@ModelAttribute("newProfile") com.i7.entity.UserProfile newProfile, Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }

        boolean success = profileCreateController.createProfile(newProfile.getName(), newProfile.getDescription());

        if (success) {
            model.addAttribute("success", "Profile created successfully.");
            model.addAttribute("newProfile", new com.i7.entity.UserProfile());
        } else {
            model.addAttribute("error", "Profile creation failed. Code might already exist.");
        }

        model.addAttribute("user", sessionUser);
        model.addAttribute("activePage", "createProfile");
        model.addAttribute("tab", "profiles");

        return "admin/createUserProfile";
    }
}
