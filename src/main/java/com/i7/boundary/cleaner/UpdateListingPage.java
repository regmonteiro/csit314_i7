package com.i7.boundary.cleaner;

import com.i7.controller.cleaner.UpdateListingController;
import com.i7.controller.cleaner.ViewListingsController;
import com.i7.controller.cleaner.ViewSuspendedListingsController;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UpdateListingPage {
    private UpdateListingController updateListingController = new UpdateListingController();
    private ViewSuspendedListingsController viewSuspendedListingsController = new ViewSuspendedListingsController();

    @GetMapping("/viewSingleListing")
    public String viewSingleListing(@RequestParam("id") int id, @RequestParam(value="success", required=false) String success, HttpSession session, Model model) {
    if ("updated".equals(success)) {
        model.addAttribute("message", "Listing updated successfully!");
    }
      return "cleaner/viewSingleListing";  // <— must match the template name exactly
    }
    @PostMapping("/updateListing")
    public String processUpdate(@ModelAttribute("listing") Listing listing,
                                HttpSession session,
                                Model model) {
        // ownership + validation
        boolean ok = Listing.updateListing(listing);
        if (ok) {
            // Redirect to the single-view page, passing id and a success flag
            return "redirect:/cleaner/viewSingleListing?id="
                + listing.getId()
                + "&success=updated";
        } else {
            model.addAttribute("error", "Could not update listing—please try again.");
            model.addAttribute("listing", listing);
            return "cleaner/updateListing";
        }
    }

        // Show the update form
        @GetMapping("/updateListing")
        public String showUpdateForm(@RequestParam("id") int id, Model model, HttpSession session) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
            Listing listing = updateListingController.getListingById(id);
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
