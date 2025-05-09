package com.i7.boundary.homeowner;

import com.i7.controller.homeowner.HOViewListingController;
import com.i7.controller.homeowner.JobRequestController;
import com.i7.controller.homeowner.ShortlistController;
import com.i7.entity.Listing;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/homeowner")
public class HOViewListingPage {
    @Autowired
    private HOViewListingController hoViewListingController;
    @Autowired
    private ShortlistController shortlistController;
    @Autowired
    private JobRequestController jobRequestController;

    @GetMapping("/viewListing")
    public String viewListing(@RequestParam("listingId") String listingId, Model model, HttpSession session) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        if ("P003".equals(user.getProfileCode())) { // Choose homeowner code
            Listing.incrementViewCount(listingId);
        }
        Map<String, String> listing = hoViewListingController.getCleanerListingDetails(listingId);
        
        if (listing == null) {
            model.addAttribute("error", "Listing not found.");
            return "errorPage";
        }
    
        model.addAttribute("user", user);
        model.addAttribute("activePage", "viewListing");
        model.addAttribute("listing", listing);
        return "homeowner/HOViewListing";
    }    

    @PostMapping("/viewListing/shortlist")
    public String shortlistListing(@RequestParam("listingId") String listingId,
                                    @RequestParam("from") String from,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P003")) {
            return "redirect:/login";
        }

        boolean success = shortlistController.addListingToShortlist(user.getUid(), listingId);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Listing successfully bookmarked.");
        } else {
            redirectAttributes.addFlashAttribute("error", "This listing is already bookmarked.");
        }

        return "redirect:/homeowner/viewListing?listingId=" + listingId + "&from=" + from;
    }


    @PostMapping("/viewListing/requestJob")
    public String requestJob(@RequestParam("listingId") String listingId,
                            @RequestParam("cleanerId") String cleanerId,
                            @RequestParam("serviceDate") String serviceDate,
                            @RequestParam("from") String from,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P003")) {
            return "redirect:/login";
        }

        boolean success = jobRequestController.createJobRequest(listingId, user.getUid(), cleanerId, serviceDate);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Job request sent successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to send job request.");
        }

        return "redirect:/homeowner/viewListing?listingId=" + listingId + "&from=" + from;
    }
}
