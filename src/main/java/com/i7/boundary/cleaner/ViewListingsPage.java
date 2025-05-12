package com.i7.boundary.cleaner;

import com.i7.controller.cleaner.SearchListingsController;
import com.i7.controller.cleaner.ViewListingsController;

import com.i7.entity.Listing;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import org.springframework.http.MediaType;
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
    private SearchListingsController searchListingsController = new SearchListingsController();
    
    // FOR ALL LISTINGS
    @GetMapping("/viewListings")
    public String viewListings(Model model,
                                HttpSession session,
                                @RequestParam(value = "success", required = false) String activeSuccess,
                                @RequestParam(value = "searchQuery", required = false) String searchQuery) {
        UserAccount user = SessionHelper.getLoggedInUser(session); // Displays all listings (active and suspended)
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("activePage", "viewListings");
        String uid = user.getUid();
        // (for search bar portion)
        List<Listing> listings = searchListingsController.getListings(uid);
        // filter by searchQuery if provided
        if (searchQuery != null && !searchQuery.isBlank()) {
            String lower = searchQuery.toLowerCase();
            listings = listings.stream()
                .filter(l ->
                    l.getTitle().toLowerCase().contains(lower) ||
                    l.getDescription().toLowerCase().contains(lower)
                )
                .collect(Collectors.toList());
        }
        model.addAttribute("listings", listings);
        // preserve the search term in the input
        model.addAttribute("searchQuery", searchQuery);
        if (activeSuccess != null) {
            model.addAttribute("activeMessage", "Listing created successfully!");
        }
        if (listings.isEmpty()) {
            model.addAttribute("activeMessage", "No active listings. Please create one!");
        }
    
        return "cleaner/viewListings";
    }

        /**
     * Return only the listings container fragment for live search updates.
     */
    @GetMapping(
        value = "/viewListings/fragment",
        produces = MediaType.TEXT_HTML_VALUE
    )
    public String viewListingsFragment(
            Model model,
            HttpSession session,
            @RequestParam(value = "q", required = false) String q) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        String uid = user.getUid();
        List<Listing> listings = viewListingsController.getListings(uid);
        if (q != null && !q.isBlank()) {
            String lower = q.toLowerCase();
            listings = listings.stream()
                .filter(l ->
                    l.getTitle().toLowerCase().contains(lower) ||
                    l.getDescription().toLowerCase().contains(lower)
                )
                .collect(Collectors.toList());
        }
        model.addAttribute("listings", listings);
        // Return the fragment identified by the container's th:fragment or id
        return "cleaner/viewListings :: listingsContainer";
    }

    // FOR SINGLE LISTING
    @GetMapping("/viewListing")
    public String viewSingleListing(@RequestParam("id") String id, Model model, HttpSession session) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        Listing listing = Listing.getListingById(id);
        if ("P003".equals(user.getProfileCode())) { // Choose homeowner code
            Listing.incrementViewCount(id);
        }
        Listing refreshedListing = Listing.getListingById(id); // refresh for new view number
        
        if (listing == null) {
            model.addAttribute("error", "Listing not found.");
            return "redirect:/cleaner/viewListings";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("activePage", "viewListings");
        model.addAttribute("listing", refreshedListing);

        return "/cleaner/viewSingleListing";
    }

}
