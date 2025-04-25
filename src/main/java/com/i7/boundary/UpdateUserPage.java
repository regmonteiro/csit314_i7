package com.i7.boundary;

import com.i7.controller.UserManageController;
import com.i7.entity.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/updateUser")
public class UpdateUserPage {

    @Autowired
    private UserManageController userManageController;

    @GetMapping
    public String showUpdateForm(@RequestParam("uid") String uid, Model model) {
        model.addAttribute("user", userManageController.getUserByUid(uid));
        model.addAttribute("profiles", userManageController.getAllProfiles());
        return "updateUser";
    }

    @PostMapping
    public String handleUpdate(@ModelAttribute("user") UserAccount updatedDetails, RedirectAttributes redirectAttributes) {
        boolean success = userManageController.updateUserDetails(updatedDetails.getUid(), updatedDetails);

        if (!success) {
            redirectAttributes.addFlashAttribute("error", "User not found or update failed.");
            return "redirect:/admin/updateUser?uid=" + updatedDetails.getUid();
        }

        redirectAttributes.addFlashAttribute("message", "User updated successfully.");
        return "redirect:/admin/viewUser?uid=" + updatedDetails.getUid();
    }
}