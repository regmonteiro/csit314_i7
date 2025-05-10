package com.i7.controller.platformManager;

import com.i7.entity.WeeklyReport;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WeeklyReportController {

    public Map<String, Object> getWeeklyReport() {
        return WeeklyReport.generateReport();
    }
}
