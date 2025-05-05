package com.i7.boundary.homeowner;

import com.i7.controller.homeowner.ShortlistSearchController;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/homeowner")
public class ViewShortlistPage {

    @Autowired
    private ShortlistSearchController shortlistSearchController;

    @GetMapping("/bookmarks")
    public String viewShortlistPage(HttpSession session, Model model) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P003")) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "homeowner/shortlist";
    }

    @PostMapping("/searchShortlist")
    public String searchShortlist(@RequestParam("keyword") String keyword, HttpSession session, Model model) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P003")) {
            return "redirect:/login";
        }

        List<Map<String, String>> results = shortlistSearchController.searchShortlist(user.getUid(), keyword);
        model.addAttribute("user", user);
        model.addAttribute("results", results);
        model.addAttribute("keyword", keyword);

        if (results.isEmpty()) {
            model.addAttribute("error", "No listings found in your bookmarks for the search term entered.");
        }

        return "homeowner/shortlist";
    }

    @GetMapping("/bookmarkDetails")
    public String viewBookmarkDetails(@RequestParam("listingId") String listingId, HttpSession session, Model model) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P003")) {
            return "redirect:/login";
        }

        Map<String, String> listing = shortlistSearchController.getListingDetails(listingId);
        if (listing == null) {
            model.addAttribute("error", "Listing not found.");
            return "error";
        }

        model.addAttribute("user", user);
        model.addAttribute("listing", listing);
        return "homeowner/shortlistDetails";
    }
}
