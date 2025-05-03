package com.i7.boundary.homeowner;

import com.i7.controller.homeowner.HOViewListingController;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/homeowner")
public class HOViewListingsPage {
    @Autowired
    private HOViewListingController controller;

    @GetMapping("/viewListing")
    public String viewListing(@RequestParam("listingId") int listingId, Model model, HttpSession session) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        Map<String, String> listing = controller.getCleanerListingDetails(listingId);
        if (listing == null) {
            model.addAttribute("error", "Listing not found.");
            return "errorPage";
        }

        model.addAttribute("user", user);
        model.addAttribute("activePage", "viewListing");
        model.addAttribute("listing", listing);
        return "homeowner/HOViewListing";
    }
}
