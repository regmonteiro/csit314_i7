package com.i7.entity;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class MonthlyReport {

    private static final String DB_URL = "jdbc:mysql://mysql-i7db.alwaysdata.net:3306/i7db_db";
    private static final String DB_USER = "i7db_admin";
    private static final String DB_PASS = "%qYyR92!N6E2";

    public static Map<String, Object> generateReport() {
        Map<String, Object> report = new HashMap<>();

        YearMonth currentMonth = YearMonth.now();
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();

        List<String> weeks = new ArrayList<>();
        List<Integer> newUsers = new ArrayList<>();
        List<Integer> newListings = new ArrayList<>();
        List<Integer> completedJobs = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            LocalDate weekStart = start;
            int weekIndex = 1;

            while (!weekStart.isAfter(end)) {
                LocalDate weekEnd = weekStart.plusDays(6);
                if (weekEnd.isAfter(end)) weekEnd = end;

                weeks.add("Week " + weekIndex++);

                newUsers.add(countByDateRange(conn, "user_accounts", "created_on", weekStart, weekEnd));
                newListings.add(countByDateRange(conn, "listings", "created_on", weekStart, weekEnd));
                completedJobs.add(countByDateRange(conn, "job_matches", "service_date", weekStart, weekEnd, "status = 'complete'"));

                weekStart = weekEnd.plusDays(1);
            }

            report.put("newUsers", newUsers.stream().mapToInt(Integer::intValue).sum());
            report.put("newListings", newListings.stream().mapToInt(Integer::intValue).sum());
            report.put("completedJobs", completedJobs.stream().mapToInt(Integer::intValue).sum());

            Map<String, Object> chart = new HashMap<>();
            chart.put("weeks", weeks);
            chart.put("newUsers", newUsers);
            chart.put("newListings", newListings);
            chart.put("completedJobs", completedJobs);
            report.put("chart", chart);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return report;
    }

    private static int countByDateRange(Connection conn, String table, String column, LocalDate from, LocalDate to) throws SQLException {
        return countByDateRange(conn, table, column, from, to, null);
    }

    private static int countByDateRange(Connection conn, String table, String column, LocalDate from, LocalDate to, String condition) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + table + " WHERE DATE(" + column + ") BETWEEN ? AND ?";
        if (condition != null) sql += " AND " + condition;

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setDate(1, java.sql.Date.valueOf(from));
        stmt.setDate(2, java.sql.Date.valueOf(to));
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }
}
