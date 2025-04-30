package com.i7.boundary.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.i7.controller.admin.UpdateUserController;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class UpdateUserPage {
   @Autowired
    private UpdateUserController updateUserController;

    @GetMapping("/updateUser")
    public String showUpdateForm(@RequestParam("uid") String uid, Model model, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }
    
        model.addAttribute("user", sessionUser); 
        model.addAttribute("activePage", "viewUsers");
        model.addAttribute("tab", "accounts");
    
        UserAccount user = updateUserController.getAccountDetails(uid);
        if (user != null) {
            model.addAttribute("userToUpdate", user);
            model.addAttribute("profiles", updateUserController.getAllProfiles());
        } else {
            model.addAttribute("error", "User not found.");
        }
    
        return "admin/updateUser";
    }
    
    @PostMapping("/updateUser")
    public String handleUpdate(@ModelAttribute UserAccount updatedUser, RedirectAttributes redirectAttributes, HttpSession session) {
        UserAccount sessionUser = SessionHelper.getLoggedInUser(session);
        if (sessionUser == null) {
            return "redirect:/login";
        }
    
        // Check if password is not empty and set it
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            updatedUser.setPassword(updatedUser.getPassword());
        }
    
        boolean success = updateUserController.updateUserDetails(updatedUser.getUid(), updatedUser);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "User updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to update user.");
        }
    
        return "redirect:/admin/viewUser?uid=" + updatedUser.getUid();
    }
    

}
