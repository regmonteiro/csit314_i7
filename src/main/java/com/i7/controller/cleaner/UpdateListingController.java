package com.i7.controller.cleaner;

import com.i7.entity.Listing;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/cleaner")
public class UpdateListingController {
  // @GetMapping("/updateListing")
  // public String showUpdateForm(@RequestParam("id") int id,
  //                              HttpSession session,
  //                              Model model) {
  //   // fetch & auth logic…
  //   model.addAttribute("listing", Listing.getListingById(id));
  //   return "cleaner/updateListing";
  // }

  @GetMapping("/viewSingleListing")
  public String viewSingleListing(
          @RequestParam("id") int id,
          @RequestParam(value="success", required=false) String success,
          HttpSession session,
          Model model) {
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
}