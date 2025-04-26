package com.i7.boundary.cleaner;

import com.i7.controller.cleaner.CreateListingController;
import com.i7.entity.Listing;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cleaner")
public class CreateListingPage {

    private CreateListingController controller = new CreateListingController();

    @GetMapping("/createListing")
    public String showCreateListingForm(Model model, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("listing", new Listing());
        model.addAttribute("activePage", "createListing");
        return "cleaner/createListing";
    }    

    @GetMapping("/createSuccess")
    public String showCreateSuccessPage(HttpSession session) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        return "cleaner/createListingSuccess";
    }


    @PostMapping("/createListing")
    public String handleCreateListing(@RequestParam String title,
                                    @RequestParam String description,
                                    @RequestParam String price,
                                    Model model,
                                    HttpSession session) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        if (title.isEmpty() || description.isEmpty() || price.isEmpty()) {
            model.addAttribute("error", "All fields are required.");
            model.addAttribute("listing", new Listing());
            return "cleaner/createListing";
        }

        double priceVal = Double.parseDouble(price);
        Listing listing = new Listing(title, description, priceVal);
        listing.setCleanerUid(user.getUid());

        boolean success = controller.createListing(listing);
        if (success) {
            return "redirect:/cleaner/createSuccess";
        } else {
            model.addAttribute("error", "Failed to create listing.");
            model.addAttribute("listing", new Listing());
            return "cleaner/createListing";
        }
    }

}
