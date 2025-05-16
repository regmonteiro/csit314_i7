package com.i7.boundary.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.i7.controller.admin.SearchProfileController;
import com.i7.controller.admin.ViewProfileController;
import com.i7.entity.UserAccount;
import com.i7.entity.UserProfile;
import com.i7.utility.SessionHelper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class ViewProfilePage {
    
    @Autowired
    private ViewProfileController viewProfileController;
    SearchProfileController searchProfileController = new SearchProfileController();

    @GetMapping("/viewProfiles")
    public String showProfiles(@RequestParam(value = "searchQuery", required = false) String searchQuery, Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            model.addAttribute("error", "No user logged in");
            return "redirect:/login";  
        }
    
        session.setAttribute("tab", "profiles");
        model.addAttribute("user", sessionUser); 
        model.addAttribute("activePage", "viewProfiles");
    
        List<UserProfile> userProfiles;
    
        if (searchQuery != null && !searchQuery.trim().isEmpty()) { 
            userProfiles = searchProfileController.searchUserProfiles(searchQuery);
            if (userProfiles.isEmpty()) {
                model.addAttribute("error", "No profiles found.");
            }
        } else {
            userProfiles = viewProfileController.getProfiles();
        }
    
        model.addAttribute("userProfiles", userProfiles);
        model.addAttribute("searchQuery", searchQuery);
    
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
    
        UserProfile profile = viewProfileController.getProfileDetails(profileCode);
        model.addAttribute("profile", profile);
    
        return "admin/viewUserProfileDetails";
    }
}
