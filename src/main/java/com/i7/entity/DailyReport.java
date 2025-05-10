package com.i7.entity;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class DailyReport {

    private static final String DB_URL = "jdbc:mysql://mysql-i7db.alwaysdata.net:3306/i7db_db";
    private static final String DB_USER = "i7db_admin";
    private static final String DB_PASS = "%qYyR92!N6E2";

    public static Map<String, Object> generateReport(LocalDate date) {
        Map<String, Object> report = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            PreparedStatement usersStmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM user_accounts WHERE DATE(created_on) = ?");
            usersStmt.setDate(1, java.sql.Date.valueOf(date));
            ResultSet usersRs = usersStmt.executeQuery();
            if (usersRs.next()) {
                report.put("newUsers", usersRs.getInt(1));
            }

            PreparedStatement listingsStmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM listings WHERE DATE(created_on) = ?");
            listingsStmt.setDate(1, java.sql.Date.valueOf(date));
            ResultSet listingsRs = listingsStmt.executeQuery();
            if (listingsRs.next()) {
                report.put("newListings", listingsRs.getInt(1));
            }

            PreparedStatement jobsStmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM job_matches WHERE status = 'complete' AND DATE(service_date) = ?");
            jobsStmt.setDate(1, java.sql.Date.valueOf(date));
            ResultSet jobsRs = jobsStmt.executeQuery();
            if (jobsRs.next()) {
                report.put("completedJobs", jobsRs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return report;
    }
}
