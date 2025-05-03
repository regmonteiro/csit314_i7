package com.i7.controller.cleaner;

import com.i7.entity.Listing;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/cleaner")
public class ViewSuspendedListingsController {

    public List<Listing> getSuspendedListings(String uid) {
        return Listing.fetchSuspendedListings(uid);
    }
    @GetMapping("/suspendListings")
    public String showSuspendedListings(HttpSession session, Model model) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        List<Listing> suspended = Listing.fetchSuspendedListings(user.getUid());
        model.addAttribute("suspendedListings", suspended);
        model.addAttribute("activePage", "suspendListings");
        model.addAttribute("user", user);
    return "cleaner/suspendListings";  // maps to src/main/resources/templates/cleaner/suspendListings.html
}
}