package com.i7.boundary;

import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class DashboardPage {

    @GetMapping("dashboard")
    public String showDashboard(
        @RequestParam(value = "tab", required = false, defaultValue = "accounts") String tab,
        HttpSession session,
        Model model) {

        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        session.setAttribute("tab", tab);
        model.addAttribute("user", user);
        model.addAttribute("activePage", "dashboard");

        String profileCode = user.getProfileCode().toLowerCase();
        switch (profileCode) {
            case "admin":
                return "admin/adminDashboard";
            case "p002":
                return "cleaner/cleanerDashboard";
            case "p003":
                return "homeowner/homeownerDashboard";
            default:
                return "redirect:/login";
        }
    }
}
