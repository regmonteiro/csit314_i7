package com.i7.entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shortlist {
    private static final String DB_URL = "jdbc:mysql://mysql-i7db.alwaysdata.net:3306/i7db_db";
    private static final String DB_USER = "i7db_admin";
    private static final String DB_PASS = "%qYyR92!N6E2";

    public static boolean saveToShortlist(String uid, String listingId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement check = conn.prepareStatement("SELECT uid FROM shortlist WHERE uid = ? AND listing_id = ?");
            check.setString(1, uid);
            check.setString(2, listingId);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                return false;
            }

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO shortlist (uid, listing_id) VALUES (?, ?)");
            stmt.setString(1, uid);
            stmt.setString(2, listingId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Map<String, String>> fetchAllShortlistedListings(String uid) {
        List<Map<String, String>> results = new ArrayList<>();
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT l.id, l.title, l.description, l.price, ua.first_name, ua.last_name, ua.email " +
                "FROM shortlist s " +
                "JOIN listings l ON s.listing_id = l.id " +
                "JOIN user_accounts ua ON l.cleaner_uid = ua.uid " +
                "WHERE s.uid = ?"
            );
            stmt.setString(1, uid);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                Map<String, String> record = new HashMap<>();
                record.put("id", rs.getString("id"));
                record.put("title", rs.getString("title"));
                record.put("description", rs.getString("description"));
                record.put("price", rs.getString("price"));
                record.put("cleanerName", rs.getString("first_name") + " " + rs.getString("last_name"));
                record.put("email", rs.getString("email"));
                results.add(record);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return results;
    }    

    public static List<Map<String, String>> searchShortlist(String uid, String keyword) {
        List<Map<String, String>> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT l.id, l.title, l.description, l.price, ua.first_name, ua.last_name, ua.email " +
                "FROM shortlist s " +
                "JOIN listings l ON s.listing_id = l.id " +
                "JOIN user_accounts ua ON l.cleaner_uid = ua.uid " +
                "WHERE s.uid = ? AND (l.title LIKE ? OR l.description LIKE ? OR ua.first_name LIKE ? OR ua.last_name LIKE ? OR ua.email LIKE ?)"
            );
            stmt.setString(1, uid);
            for (int i = 2; i <= 6; i++) {
                stmt.setString(i, "%" + keyword + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, String> record = new HashMap<>();
                record.put("id", rs.getString("id"));
                record.put("title", rs.getString("title"));
                record.put("description", rs.getString("description"));
                record.put("price", rs.getString("price"));
                record.put("cleanerName", rs.getString("first_name") + " " + rs.getString("last_name"));
                record.put("email", rs.getString("email"));
                results.add(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public static Map<String, String> getListingWithCleanerDetails(String listingId) {
        Map<String, String> result = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT l.title, l.description, l.price, ua.first_name, ua.last_name, ua.email " +
                "FROM listings l " +
                "JOIN user_accounts ua ON l.cleaner_uid = ua.uid " +
                "WHERE l.id = ?"
            );
            stmt.setString(1, listingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                result.put("title", rs.getString("title"));
                result.put("description", rs.getString("description"));
                result.put("price", rs.getString("price"));
                result.put("cleanerName", rs.getString("first_name") + " " + rs.getString("last_name"));
                result.put("email", rs.getString("email"));
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }
}
