package com.i7.controller.platformManager;

import com.i7.entity.DailyReport;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
public class DailyReportController {
    public Map<String, Object> getDailyReport() {
        return DailyReport.generateReport(LocalDate.now());
    }
}
