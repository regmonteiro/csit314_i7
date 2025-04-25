package com.i7.boundary.cleaner;

import com.i7.controller.cleaner.CreateListingController;
import com.i7.entity.Listing;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cleaner")
public class CreateListingPage {

    private CreateListingController controller = new CreateListingController();

    @GetMapping("/createListing")
    public String showForm(Model model) {
        model.addAttribute("listing", new Listing());
        return "cleaner/createListing";
    }

    @PostMapping("/createListing")
    public String handleCreateListing(@RequestParam String title,
                                      @RequestParam String description,
                                      @RequestParam String price,
                                      Model model) {

        if (title.isEmpty() || description.isEmpty() || price.isEmpty()) {
            model.addAttribute("error", "All fields are required.");
            return "cleaner/createListing";
        }

        double priceVal = Double.parseDouble(price);
        Listing listing = new Listing(title, description, priceVal);

        boolean success = controller.createListing(listing);
        if (success) {
            model.addAttribute("success", "Listing created successfully.");
        } else {
            model.addAttribute("error", "Failed to create listing.");
        }

        return "cleaner/createListing";
    }
}