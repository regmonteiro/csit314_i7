package com.i7.controller.platformManager;

import com.i7.entity.MonthlyReport;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MonthlyReportController {

    public Map<String, Object> getMonthlyReport() {
        return MonthlyReport.generateReport();
    }
}
