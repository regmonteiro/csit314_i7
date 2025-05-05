package com.i7.boundary.cleaner;

import com.i7.controller.cleaner.SuspendListingController;
import com.i7.entity.Listing;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cleaner")
public class SuspendListingsPage {
    private SuspendListingController viewSuspendedListingsController = new SuspendListingController();

    @GetMapping("/suspendListing") 
    public String showSuspendedListings(HttpSession session, Model model) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        String uid = user.getUid();
        Listing suspended = viewSuspendedListingsController.getListingById(uid);
        model.addAttribute("suspendedListings", suspended);
        model.addAttribute("activePage", "suspendListings");
        model.addAttribute("user", user);
    return "cleaner/suspendListing";  
    }


}
