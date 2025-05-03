package com.i7.boundary.homeowner;

import com.i7.controller.homeowner.JobRequestController;
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
public class JobRequestPage {

    @Autowired
    private JobRequestController jobRequestController;

    @GetMapping("/bookings")
    public String showPendingJobs(Model model, HttpSession session) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P003")) {
            return "redirect:/login";
        }

        List<Map<String, String>> jobMatches = jobRequestController.getJobMatchesByHomeowner(user.getUid());
        model.addAttribute("user", user);
        model.addAttribute("jobMatches", jobMatches);
        model.addAttribute("activePage", "bookings");
        return "/homeowner/hoJobRequests";
    }

    @PostMapping("/completeJob")
    public String completeJob(@RequestParam("matchId") String matchId, HttpSession session, Model model) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P003")) {
            return "redirect:/login";
        }

        boolean success = jobRequestController.markHomeownerCompleted(matchId, user.getUid());
        if (!success) {
            model.addAttribute("error", "Unable to mark job as completed.");
        }

        return "redirect:/homeowner/bookings";
    }

    @GetMapping("/jobDetails")
    public String showJobDetails(@RequestParam("matchId") String matchId, Model model, HttpSession session) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P003")) {
            return "redirect:/login";
        }

        Map<String, String> job = jobRequestController.getMatchById(matchId);
        if (job == null) {
            model.addAttribute("error", "Job not found.");
            model.addAttribute("user", user);
            return "error";
        }

        model.addAttribute("user", user);
        model.addAttribute("job", job);
        return "homeowner/hoJobDetails";
    }
}
