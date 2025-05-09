package com.i7.boundary.cleaner;

import com.i7.controller.cleaner.SuspendListingController;
import com.i7.entity.Listing;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cleaner")
public class SuspendListingsPage {
    private SuspendListingController SuspendedListingController = new SuspendListingController();

    @GetMapping("/suspendListing")
    public String suspendListing(@RequestParam("id") String id, Model model, HttpSession session) {
    UserAccount user = SessionHelper.getLoggedInUser(session);
    if (user == null) {
        return "redirect:/login";
    }
        Listing listing = SuspendedListingController.getListingById(id);
        if (listing == null) {
            model.addAttribute("error", "Listing not found.");
            return "redirect:/cleaner/viewListings";
        }
        model.addAttribute("user", user);
        model.addAttribute("activePage", "viewSingleListings");
        model.addAttribute("listing", listing);
        return "/cleaner/suspendListing";
    }
    @PostMapping("/suspendListing")
    public String suspendListingPost(@ModelAttribute("listing") Listing listing,
                                HttpSession session,
                                Model model) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        boolean ok = SuspendedListingController.suspendListing(listing);
        if (ok) {
            return "redirect:/cleaner/viewSingleListing?id="
                + listing.getId()
                + "&success=updated";
        } else {
            model.addAttribute("error", "Could not update listing — please try again.");
            model.addAttribute("listing", listing);
            return "cleaner/viewSingleListing";
        }
    }

}
