package com.i7.entity;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class JobMatch {

    private static final String DB_URL = "jdbc:mysql://mysql-i7db.alwaysdata.net:3306/i7db_db";
    private static final String DB_USER = "i7db_admin";
    private static final String DB_PASS = "%qYyR92!N6E2";
    
    // cleaner
    public static Map<String, String> fetchJobDetails(String matchId) {
        Map<String, String> record = new HashMap<>();
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT jm.id, jm.service_date, jm.status, jm.cleaner_marked_complete, " +
                "ua.first_name, ua.last_name, ua.email, l.title " +
                "FROM job_matches jm " +
                "JOIN user_accounts ua ON jm.homeowner_uid = ua.uid " +
                "JOIN listings l ON jm.listing_id = l.id " +
                "WHERE jm.id = ?"
            );
            stmt.setString(1, matchId);
    
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                record.put("id", rs.getString("id"));
                record.put("listingTitle", rs.getString("title"));
                record.put("serviceDate", rs.getString("service_date"));
                record.put("status", rs.getString("status"));
                record.put("homeownerName", rs.getString("first_name") + " " + rs.getString("last_name"));
                record.put("homeownerEmail", rs.getString("email"));
                record.put("cleanerCompleted", String.valueOf(rs.getBoolean("cleaner_marked_complete")));
            } else {
                return null;
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return record;
    }

    public static List<Map<String, String>> fetchPendingJobs(String cleanerUid) {
        List<Map<String, String>> pendingJobs = new ArrayList<>();
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT jm.id, jm.service_date, jm.status, " +
                "ua.first_name AS homeowner_name, ua.last_name AS homeowner_last_name, ua.email AS homeowner_email, l.title AS listing_title " +
                "FROM job_matches jm " +
                "JOIN user_accounts ua ON jm.homeowner_uid = ua.uid " +
                "JOIN listings l ON jm.listing_id = l.id " +
                "WHERE jm.cleaner_uid = ? AND jm.status = 'pending'"
            );
    
            stmt.setString(1, cleanerUid);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                Map<String, String> record = new HashMap<>();
                record.put("id", rs.getString("id"));
                record.put("serviceDate", rs.getString("service_date"));
                record.put("status", rs.getString("status"));
                record.put("homeownerName", rs.getString("homeowner_name") + " " + rs.getString("homeowner_last_name"));
                record.put("homeownerEmail", rs.getString("homeowner_email"));
                record.put("listingTitle", rs.getString("listing_title"));
                pendingJobs.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return pendingJobs;
    }
    
    public static List<Map<String, String>> fetchConfirmedJobs(String uid) {
        List<Map<String, String>> matches = new ArrayList<>();
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT jm.id, jm.service_date, jm.status, jm.cleaner_marked_complete, " + // ✅ added field
                "ua.first_name AS homeowner_name, ua.last_name AS homeowner_last_name, ua.email AS homeowner_email, l.title AS listing_title " +
                "FROM job_matches jm " +
                "JOIN user_accounts ua ON jm.homeowner_uid = ua.uid " +
                "JOIN listings l ON jm.listing_id = l.id " +
                "WHERE jm.cleaner_uid = ? AND jm.status = 'confirmed'"
            );
            stmt.setString(1, uid);
    
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, String> record = new HashMap<>();
                record.put("id", rs.getString("id"));
                record.put("serviceDate", rs.getString("service_date"));
                record.put("status", rs.getString("status"));
                record.put("homeownerName", rs.getString("homeowner_name") + " " + rs.getString("homeowner_last_name"));
                record.put("homeownerEmail", rs.getString("homeowner_email"));
                record.put("listingTitle", rs.getString("listing_title"));
                record.put("cleanerCompleted", String.valueOf(rs.getBoolean("cleaner_marked_complete"))); // ✅ added
                matches.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return matches;
    }    
    
    public static List<Map<String, String>> fetchCompletedJobs(String cleanerUid) {
        List<Map<String, String>> completedJobs = new ArrayList<>();
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT jm.id, jm.service_date, jm.status, " +
                "ua.first_name AS homeowner_name, ua.last_name AS homeowner_last_name, ua.email AS homeowner_email, l.title AS listing_title " +
                "FROM job_matches jm " +
                "JOIN user_accounts ua ON jm.homeowner_uid = ua.uid " +
                "JOIN listings l ON jm.listing_id = l.id " +
                "WHERE jm.cleaner_uid = ? AND jm.status = 'complete'"
            );
    
            stmt.setString(1, cleanerUid);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                Map<String, String> record = new HashMap<>();
                record.put("id", rs.getString("id"));
                record.put("serviceDate", rs.getString("service_date"));
                record.put("status", rs.getString("status"));
                record.put("homeownerName", rs.getString("homeowner_name") + " " + rs.getString("homeowner_last_name"));
                record.put("homeownerEmail", rs.getString("homeowner_email"));
                record.put("listingTitle", rs.getString("listing_title"));
                completedJobs.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return completedJobs;
    }

    public static boolean changeJobStatus(String matchId, String status) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE job_matches SET status = ? WHERE id = ?"
            );
            stmt.setString(1, status);  
            stmt.setString(2, matchId); 
            int updated = stmt.executeUpdate();
            return updated > 0; 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateCleanerCompleted(String matchId, String cleanerUid) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE job_matches SET cleaner_marked_complete = TRUE WHERE id = ? AND cleaner_uid = ?"
            );
            stmt.setString(1, matchId);
            stmt.setString(2, cleanerUid);
            int updated = stmt.executeUpdate();
    
            if (updated > 0) {
                checkAndSetComplete(conn, matchId);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Map<String, String>> searchPastJobs(String cleanerUid, String keyword) {
        List<Map<String, String>> results = new ArrayList<>();
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT jm.id, jm.service_date, jm.status, " +
                "ua.first_name AS homeowner_name, ua.last_name AS homeowner_last_name, l.title AS listing_title " +
                "FROM job_matches jm " +
                "JOIN user_accounts ua ON jm.homeowner_uid = ua.uid " +
                "JOIN listings l ON jm.listing_id = l.id " +
                "WHERE jm.cleaner_uid = ? AND jm.status = 'complete' AND " +
                "(ua.first_name LIKE ? OR ua.last_name LIKE ? OR jm.service_date LIKE ? OR l.title LIKE ?)"
            );            
            stmt.setString(1, cleanerUid);
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            stmt.setString(4, "%" + keyword + "%");
            stmt.setString(5, "%" + keyword + "%");
            
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                Map<String, String> record = new HashMap<>();
                record.put("id", rs.getString("id"));
                record.put("serviceDate", rs.getString("service_date"));
                record.put("status", rs.getString("status"));
                record.put("homeownerName", rs.getString("homeowner_name") + " " + rs.getString("homeowner_last_name"));
                record.put("listingTitle", rs.getString("listing_title"));
                results.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return results;
    }
    

    //homeowner
    public static boolean insertJobMatch(String listingId, String homeownerUid, String cleanerUid, LocalDate serviceDate) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String matchId = generateUniqueMatchId(conn);
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO job_matches (id, listing_id, homeowner_uid, cleaner_uid, service_date, status) " +
                "VALUES (?, ?, ?, ?, ?, 'pending')"
            );
            stmt.setString(1, matchId);
            stmt.setString(2, listingId);
            stmt.setString(3, homeownerUid);
            stmt.setString(4, cleanerUid);
            stmt.setDate(5, java.sql.Date.valueOf(serviceDate));
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Map<String, String>> getMatchesByHomeowner(String homeownerUid) {
        List<Map<String, String>> jobs = new ArrayList<>();
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
               "SELECT jm.id, jm.service_date, jm.status, jm.homeowner_marked_complete, " +
                "ua.first_name AS cleaner_name, ua.last_name AS cleaner_last_name, l.title AS listing_title " +
                "FROM job_matches jm " +
                "JOIN user_accounts ua ON jm.cleaner_uid = ua.uid " +
                "JOIN listings l ON jm.listing_id = l.id " +
                "WHERE jm.homeowner_uid = ?"
            );
    
            stmt.setString(1, homeownerUid);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                Map<String, String> job = new HashMap<>();
                job.put("id", rs.getString("id"));
                job.put("serviceDate", rs.getString("service_date"));
                job.put("status", rs.getString("status"));
                job.put("homeownerCompleted", String.valueOf(rs.getBoolean("homeowner_marked_complete")));
                job.put("cleanerName", rs.getString("cleaner_name") + " " + rs.getString("cleaner_last_name"));
                job.put("listingTitle", rs.getString("listing_title"));
                jobs.add(job);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return jobs;
    }
    
    public static boolean updateHomeownerCompleted(String matchId, String homeownerUid) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE job_matches SET homeowner_marked_complete = TRUE WHERE id = ? AND homeowner_uid = ?"
            );
            stmt.setString(1, matchId);
            stmt.setString(2, homeownerUid);
            int updated = stmt.executeUpdate();
    
            if (updated > 0) {
                checkAndSetComplete(conn, matchId);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //misc
    private static void checkAndSetComplete(Connection conn, String matchId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT homeowner_marked_complete, cleaner_marked_complete FROM job_matches WHERE id = ?"
        );
        stmt.setString(1, matchId);
        ResultSet rs = stmt.executeQuery();
    
        if (rs.next()) {
            boolean homeownerDone = rs.getBoolean("homeowner_marked_complete");
            boolean cleanerDone = rs.getBoolean("cleaner_marked_complete");
    
            // if both parties mark complete, then complete
            if (homeownerDone && cleanerDone) {
                PreparedStatement updateStatus = conn.prepareStatement(
                    "UPDATE job_matches SET status = 'complete' WHERE id = ?"
                );
                updateStatus.setString(1, matchId);
                updateStatus.executeUpdate();
            }
        }
    }
    
    private static String generateUniqueMatchId(Connection conn) throws SQLException {
        String id;
        do {
            id = String.valueOf((long) (Math.random() * 1_000_000_0000L));
            PreparedStatement check = conn.prepareStatement("SELECT id FROM job_matches WHERE id = ?");
            check.setString(1, id);
            ResultSet rs = check.executeQuery();
            if (!rs.next()) break; // unique
        } while (true);
        return id;
    }    

}
