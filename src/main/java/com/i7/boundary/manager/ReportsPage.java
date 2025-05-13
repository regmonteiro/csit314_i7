package com.i7.boundary.manager;

import com.i7.controller.platformManager.DailyReportController;
import com.i7.controller.platformManager.MonthlyReportController;
import com.i7.controller.platformManager.WeeklyReportController;
import com.i7.entity.UserAccount;
import com.i7.utility.SessionHelper;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/manage/report")
public class ReportsPage {

    @Autowired
    private DailyReportController dailyReportController;

    @Autowired
    private WeeklyReportController weeklyReportController;

    @Autowired
    private MonthlyReportController monthlyReportController;

    @GetMapping("/{type}")
    public String viewReport(@PathVariable("type") String type,
                             Model model, HttpSession session) {

        UserAccount user = SessionHelper.getLoggedInUser(session);
        if (user == null || !user.getProfileCode().equals("P004")) {
            return "redirect:/login";
        }

        Map<String, Object> report;

        switch (type.toLowerCase()) {
            case "weekly":
                report = weeklyReportController.getWeeklyReport();
                model.addAttribute("chart", report.get("chart"));
                break;
            case "monthly":
                report = monthlyReportController.getMonthlyReport();
                model.addAttribute("chart", report.get("chart"));
                break;
            case "daily":
            default:
                report = dailyReportController.getDailyReport();
                break;
        }

        model.addAttribute("user", user);
        model.addAttribute("report", report);
        model.addAttribute("activeTab", type.toLowerCase());
        model.addAttribute("activePage", "report");

        return "manager/reportDashboard";
    }
}
