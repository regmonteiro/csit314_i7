package com.i7.boundary.cleaner;

import com.i7.controller.cleaner.UpdateListingController;
import com.i7.entity.Listing;
import com.i7.entity.ServiceCategory;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/cleaner")
public class UpdateListingPage {
    private UpdateListingController updateListingController = new UpdateListingController();
    
    @GetMapping("/updateListing")
    public String updateListing(@RequestParam("id") String id, Model model, HttpSession session) {
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
        model.addAttribute("categories", updateListingController.getAllCategories());
        model.addAttribute("listing", listing);
        return "cleaner/updateListing";
    }
    @PostMapping("/updateListing")
    public String updateListingPost(@ModelAttribute("listing") Listing listing, HttpSession session, RedirectAttributes redirectAttributes) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        boolean ok = updateListingController.setListing(listing.getId(), listing);
        if (ok) {
            redirectAttributes.addFlashAttribute("message", "Update Successful");
        } else {
            redirectAttributes.addAttribute("error", "Could not update listing—please try again.");
        }
        return "redirect:/cleaner/viewListing?id="+ listing.getId();
    }
}
