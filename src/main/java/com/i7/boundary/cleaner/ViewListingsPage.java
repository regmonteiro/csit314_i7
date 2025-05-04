package com.i7.boundary.cleaner;

import com.i7.controller.cleaner.ViewListingsController;
import com.i7.entity.Listing;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cleaner")
public class ViewListingsPage {

    private ViewListingsController controller = new ViewListingsController();

    @GetMapping("/viewListings")
    public String viewListings(Model model,
                                HttpSession session,
                                @RequestParam(value = "success", required = false) String success) {
        UserAccount user = SessionHelper.getLoggedInUser(session); // Displays all listings (active and suspended)
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("activePage", "viewListings");

        String uid = user.getUid();
        List<Listing> listings = controller.getListings(uid);
        model.addAttribute("listings", listings);
        if (listings.isEmpty()) {
            model.addAttribute("message", "No active listings. Please create one!");
        } 
        if (success != null) {
            model.addAttribute("success", "Listing created successfully.");
        }
        return "cleaner/viewListings";
    }

    @GetMapping("/viewListing")
    public String viewSingleListing(@RequestParam("id") String id, Model model, HttpSession session) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        Listing listing = Listing.getListingById(id);
        if (listing == null) {
            model.addAttribute("error", "Listing not found.");
            return "redirect:/cleaner/viewListings";
        }
        model.addAttribute("user", user);
        model.addAttribute("activePage", "viewListings");
        model.addAttribute("listing", listing);

        return "cleaner/viewSingleListing";
    }

        // Show the update form
    @GetMapping("/updateListing")
    public String showUpdateForm(@RequestParam("id") String id, Model model, HttpSession session) {
    UserAccount user = SessionHelper.getLoggedInUser(session);
    if (user == null) {
        return "redirect:/login";
    }
        Listing listing = Listing.getListingById(id);
        if (listing == null) {
            model.addAttribute("error", "Listing not found.");
            return "redirect:/cleaner/viewListings";
        }
        model.addAttribute("user", user);
        model.addAttribute("activePage", "viewListings");
        model.addAttribute("listing", listing);
        return "cleaner/updateListingForm";
    }
    
}
