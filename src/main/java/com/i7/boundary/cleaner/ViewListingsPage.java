package com.i7.boundary.cleaner;


import com.i7.controller.cleaner.ViewListingsController;
import com.i7.controller.cleaner.ViewSuspendedListingsController;
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
    private ViewSuspendedListingsController viewSuspendedListingsController = new ViewSuspendedListingsController();
    
    // FOR ALL LISTINGS
    @GetMapping("/viewListings")
    public String viewListings(Model model,
                                HttpSession session,
                                @RequestParam(value = "success", required = false) String activeSuccess, String suspendedSuccess) {
        UserAccount user = SessionHelper.getLoggedInUser(session); // Displays all listings (active and suspended)
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("activePage", "viewListings");
        String uid = user.getUid();
        // (for search bar portion)
        List<Listing> listings = viewListingsController.getListings(uid);
        List<Listing> suspendedListings = viewSuspendedListingsController.getSuspendedListings(uid);
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
        model.addAttribute("suspendedListings", suspendedListings);
        if (activeSuccess != null) {
            model.addAttribute("activeMessage", "Listing created successfully!");
        }
        if (suspendedSuccess != null) {
            model.addAttribute("suspendedMessage", "Listing suspended successfully!");
        }
        if (listings.isEmpty()) {
            model.addAttribute("activeMessage", "No active listings. Please create one!");
        }
        if (suspendedListings.isEmpty()) {
            model.addAttribute("suspendedMessage", "No suspended listings.");
        }
    
        return "cleaner/viewListings";
    }

    @GetMapping("/suspendedListings") //  WRONG needs to be single page
    public String showSuspendedListings(HttpSession session, Model model) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        String uid = user.getUid();
        List<Listing> suspended = viewSuspendedListingsController.getSuspendedListings(uid);
        model.addAttribute("suspendedListings", suspended);
        model.addAttribute("activePage", "suspendListings");
        model.addAttribute("user", user);
    return "cleaner/suspendedListings";  // maps to src/main/resources/templates/cleaner/suspendListings.html
    }

    // FOR SINGLE LISTING
    @GetMapping("/viewListing")
    public String viewSingleListing(@RequestParam("id") int id, Model model, HttpSession session) {
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

}
