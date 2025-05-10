package com.i7.entity;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class WeeklyReport {

    private static final String DB_URL = "jdbc:mysql://mysql-i7db.alwaysdata.net:3306/i7db_db";
    private static final String DB_USER = "i7db_admin";
    private static final String DB_PASS = "%qYyR92!N6E2";

    public static Map<String, Object> generateReport() {
        Map<String, Object> report = new HashMap<>();
    
        List<String> days = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
        List<Integer> newUsers = new ArrayList<>();
        List<Integer> newListings = new ArrayList<>();
        List<Integer> completedJobs = new ArrayList<>();
    
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(java.time.DayOfWeek.MONDAY);
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            for (int i = 0; i < 7; i++) {
                LocalDate date = monday.plusDays(i);
    
                // Count new users
                PreparedStatement userStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM user_accounts WHERE created_on = ?");
                userStmt.setDate(1, java.sql.Date.valueOf(date));
                ResultSet ur = userStmt.executeQuery();
                newUsers.add(ur.next() ? ur.getInt(1) : 0);
    
                // Count new listings
                PreparedStatement listStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM listings WHERE DATE(created_on) = ?");
                listStmt.setDate(1, java.sql.Date.valueOf(date));
                ResultSet lr = listStmt.executeQuery();
                newListings.add(lr.next() ? lr.getInt(1) : 0);
    
                // Count completed jobs
                PreparedStatement jobStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM job_matches WHERE status = 'complete' AND DATE(service_date) = ?");
                jobStmt.setDate(1, java.sql.Date.valueOf(date));
                ResultSet jr = jobStmt.executeQuery();
                completedJobs.add(jr.next() ? jr.getInt(1) : 0);
            }
    
            report.put("newUsers", newUsers.stream().mapToInt(Integer::intValue).sum());
            report.put("newListings", newListings.stream().mapToInt(Integer::intValue).sum());
            report.put("completedJobs", completedJobs.stream().mapToInt(Integer::intValue).sum());
    
            Map<String, Object> chart = new HashMap<>();
            chart.put("days", days);
            chart.put("newUsers", newUsers);
            chart.put("newListings", newListings);
            chart.put("completedJobs", completedJobs);
            report.put("chart", chart);
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return report;
    }
    
}
