package com.i7.boundary.cleaner;

import com.i7.controller.cleaner.CleanerJobController;
import com.i7.controller.cleaner.PastJobSearchController;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cleaner")
public class CleanerJobPage {

    @Autowired
    private CleanerJobController controller;
    @Autowired
    private PastJobSearchController pastJobSearchController;

    @GetMapping("/jobRequests")
    public String viewPendingJobs(HttpSession session, Model model) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P002")) {
            return "redirect:/login";
        }

        List<Map<String, String>> pendingJobs = controller.getPendingJobs(user.getUid());
        model.addAttribute("user", user);
        model.addAttribute("jobMatches", pendingJobs);
        model.addAttribute("activePage", "jobRequests");
        return "cleaner/jobRequests";
    }

    @GetMapping("/jobDetails")
    public String viewJobDetails(@RequestParam("matchId") String matchId, HttpSession session, Model model) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P002")) {
            return "redirect:/login";
        }

        Map<String, String> job = controller.getJobDetails(matchId);
        if (job == null) {
            model.addAttribute("error", "Job not found.");
            model.addAttribute("user", user);
            return "errorPage";
        }

        model.addAttribute("user", user);
        model.addAttribute("job", job);
        model.addAttribute("activePage", "jobDetails");

        String jobStatus = job.get("status");
        model.addAttribute("jobStatus", jobStatus);

        if ("pending".equals(jobStatus)) {
            model.addAttribute("action", "Confirm Job");
            model.addAttribute("actionUrl", "/cleaner/confirmJob");
        } else if ("confirmed".equals(jobStatus)) {
            model.addAttribute("action", "Mark as Completed");
            model.addAttribute("actionUrl", "/cleaner/completeJob");
        } else {
            model.addAttribute("action", null);
        }

        return "cleaner/jobDetails";
    }

    @PostMapping("/confirmJob")
    public String confirmJob(@RequestParam("matchId") String matchId, HttpSession session, RedirectAttributes redirectAttributes) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P002")) {
            return "redirect:/login";
        }

        boolean success = controller.confirmJob(matchId);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Job confirmed successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Unable to confirm job.");
        }

        return "redirect:/cleaner/jobRequests";
    }

    @PostMapping("/completeJob")
    public String completeJob(@RequestParam("matchId") String matchId, HttpSession session, RedirectAttributes redirectAttributes) {
        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P002")) {
            return "redirect:/login";
        }
    
        boolean success = controller.completeJob(matchId, user.getUid());
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Job marked as completed.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Unable to mark job as completed.");
        }
    
        return "redirect:/cleaner/jobRequests";
    }
    

    @GetMapping("/jobHistory")
    public String showCompletedJobs(HttpSession session, Model model) {
        UserAccount cleaner = SessionHelper.getLoggedInUser(session);
        if (cleaner == null || !cleaner.getProfileCode().equals("P002")) {
            return "redirect:/login";
        }

        List<Map<String, String>> completedJobs = controller.getCompletedJobs(cleaner.getUid());
        model.addAttribute("user", cleaner);
        model.addAttribute("activePage", "jobHistory");
        model.addAttribute("jobMatches", completedJobs);
        return "cleaner/jobHistory";
    }

    @GetMapping("/jobHistory/search")
    public String searchCompletedJobs(@RequestParam("keyword") String keyword, HttpSession session, Model model) {
        UserAccount cleaner = SessionHelper.getLoggedInUser(session);
        if (cleaner == null || !cleaner.getProfileCode().equals("P002")) {
            return "redirect:/login";
        }

        List<Map<String, String>> results = pastJobSearchController.searchPastJobs(cleaner.getUid(), keyword);

        model.addAttribute("user", cleaner);
        model.addAttribute("activePage", "jobHistory");
        model.addAttribute("jobMatches", results);
        model.addAttribute("searchKeyword", keyword);

        return "cleaner/jobHistory"; // same view reused
    }
}
