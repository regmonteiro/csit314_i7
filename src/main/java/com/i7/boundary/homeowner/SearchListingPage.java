package com.i7.boundary.homeowner;

import com.i7.controller.homeowner.HOSearchListingController;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/homeowner")
public class SearchListingPage {

    @Autowired
    private HOSearchListingController hoSearchListingController;

    @GetMapping("/searchListings")
    public String searchListings(@RequestParam(required = false) String searchQuery,
                                 Model model,
                                 HttpSession session) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P003")) {
            return "redirect:/login";
        }

        List<Map<String, String>> results = new ArrayList<>();
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            results = hoSearchListingController.searchListings(searchQuery.trim());
        }
        model.addAttribute("user", user);
        model.addAttribute("results", results);
        model.addAttribute("activePage", "searchListings");
        return "homeowner/searchListings";
    }
}
