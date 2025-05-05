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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cleaner")
public class ViewListingsPage {

    private ViewListingsController viewListingsController = new ViewListingsController();

    
    // FOR ALL LISTINGS
    @GetMapping("/viewListings")
    public String viewListings(Model model,
                                HttpSession session,
                                @RequestParam(value = "success", required = false) String activeSuccess) {
        UserAccount user = SessionHelper.getLoggedInUser(session); // Displays all listings (active and suspended)
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("activePage", "viewListings");
        String uid = user.getUid();
        // (for search bar portion)
        List<Listing> listings = viewListingsController.getListings(uid);
        //for search bar
        if (activeSuccess != null && !activeSuccess.isBlank()) {
            String lower = activeSuccess.toLowerCase();
            listings = listings.stream()
                .filter(l ->
                    l.getTitle().toLowerCase().contains(lower) ||
                    l.getDescription().toLowerCase().contains(lower)
                )
                .collect(Collectors.toList());
        }
        // can add suspendedSuccess if too many susListings
        model.addAttribute("listings", listings);
        if (activeSuccess != null) {
            model.addAttribute("activeMessage", "Listing created successfully!");
        }
        if (listings.isEmpty()) {
            model.addAttribute("activeMessage", "No active listings. Please create one!");
        }
    
        return "cleaner/viewListings";
    }

    // FOR SINGLE LISTING
    @GetMapping("/viewListing")
    public String viewSingleListing(@RequestParam("id") int id, Model model, HttpSession session) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        Listing.incrementViewCount(id);
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

}
