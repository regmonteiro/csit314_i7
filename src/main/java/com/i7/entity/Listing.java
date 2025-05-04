package com.i7.entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Listing {
    private static final String DB_URL = "jdbc:mysql://mysql-i7db.alwaysdata.net:3306/i7db_db";
    private static final String DB_USER = "i7db_admin";
    private static final String DB_PASS = "%qYyR92!N6E2";

    private int id;
    private String title;
    private String description;
    private double price;
    private String cleanerUid;
    private String status = "active";

    // Getters and Setters
    public int getId() {return id;  }
    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public double getPrice() {return price;}
    public String getCleanerUid() {return cleanerUid;}
    public String getStatus() {return status;}

    public void setId(int id) {this.id = id;}
    public void setTitle(String title) {this.title = title;}
    public void setDescription(String description) {this.description = description;}
    public void setPrice(double price) {this.price = price;}
    public void setCleanerUid(String cleanerUid) {this.cleanerUid = cleanerUid;}
    public void setStatus(String status) {this.status= status;}

    // Constructors
    public Listing() {}

    public Listing(String title, String description, double price) {
        this.title = title;
        this.description = description;
        this.price = price;
        status = "active";
    }

    private static String generateUniqueListingId(Connection conn) throws SQLException {
        String id;
        boolean isUnique;
    
        do {
            id = String.format("%010d", (long)(Math.random() * 1_000_000_0000L));
            PreparedStatement check = conn.prepareStatement("SELECT id FROM listings WHERE id = ?");
            check.setString(1, id);
            ResultSet rs = check.executeQuery();
            isUnique = !rs.next();
        } while (!isUnique);
    
        return id;
    }    

    // Save Listing to DB
    public boolean createListing() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO listings (id, title, description, price, cleaner_uid) VALUES (?, ?, ?, ?,?)");
            stmt.setString(1, generateUniqueListingId(conn));
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setDouble(4, price);
            stmt.setString(5, cleanerUid);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Listing> fetchListings(String uid) {
        List<Listing> listings = new ArrayList<>();
    
        String sql = """
            SELECT id, title, description, price, cleaner_uid, status_code
              FROM listings 
             WHERE cleaner_uid = ? 
               AND status_code <> 'suspended'
        """;
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, uid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Listing l = new Listing();
                l.setId(rs.getInt("id"));
                l.setTitle(rs.getString("title"));
                l.setDescription(rs.getString("description"));
                l.setPrice(rs.getDouble("price"));
                l.setCleanerUid(rs.getString("cleaner_uid"));
                l.setStatus(rs.getString("status"));
                listings.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return listings;
    }

    public static List<Listing> fetchSuspendedListings(String uid) {
        List<Listing> suspendedListings = new ArrayList<>();
    
        String sql = """
            SELECT id, title, description, price, cleaner_uid, status
              FROM listings
             WHERE cleaner_uid = ?
               AND status = 'suspended'
        """;
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, uid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Listing l = new Listing();
                l.setId(rs.getInt("id"));
                l.setTitle(rs.getString("title"));
                l.setDescription(rs.getString("description"));
                l.setPrice(rs.getDouble("price"));
                l.setCleanerUid(rs.getString("cleaner_uid"));
                l.setStatus(rs.getString("status"));
                suspendedListings.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return suspendedListings;
    }

    public static Listing getListingById(String id2) {
        Listing listing = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM listings WHERE id = ?");
            stmt.setString(1, id2);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                listing = new Listing();
                listing.setId(rs.getInt("id"));
                listing.setTitle(rs.getString("title"));
                listing.setDescription(rs.getString("description"));
                listing.setPrice(rs.getDouble("price"));
                listing.setCleanerUid(rs.getString("cleaner_uid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listing;
    }

    /**
     * Update an existing listing in the database.
     * @param listing the Listing object containing updated data (must include id)
     * @return true if the update succeeded, false otherwise
     */
    public static boolean updateListing(Listing listing) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement("UPDATE listings SET title = ?, description = ?, price = ?, cleaner_uid = ? WHERE id = ?")) {
            
            stmt.setString(1, listing.getTitle());
            stmt.setString(2, listing.getDescription());
            stmt.setDouble(3, listing.getPrice());
            stmt.setString(4, listing.getCleanerUid());
            stmt.setInt(5, listing.getId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // May need to put suspendListing here

    // homeowner functions
    public static List<Map<String, String>> searchWithKeyword(String searchQuery) {
        List<Map<String, String>> results = new ArrayList<>();
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT l.id, l.title, l.price, u.first_name, u.last_name " +
                "FROM listings l " +
                "JOIN user_accounts u ON l.cleaner_uid = u.uid " +
                "WHERE l.title LIKE ?"
            );
            stmt.setString(1, "%" + searchQuery + "%");
    
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, String> record = new HashMap<>();
                record.put("id", String.valueOf(rs.getInt("id")));
                record.put("title", rs.getString("title"));
                record.put("price", rs.getString("price"));
                record.put("cleanerName", rs.getString("first_name") + " " + rs.getString("last_name"));
                results.add(record);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return results;
    }
    
    
    public static Map<String, String> getCleanerListingDetails(String listingId) {
        Map<String, String> record = new HashMap<>();
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT l.id, l.title, l.description, l.price, l.cleaner_uid, u.first_name, u.last_name, u.email " +
                "FROM listings l " +
                "JOIN user_accounts u ON l.cleaner_uid = u.uid " +
                "WHERE l.id = ?"
            );
            stmt.setString(1, listingId);
    
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                record.put("id", String.valueOf(rs.getInt("id")));
                record.put("title", rs.getString("title") != null ? rs.getString("title") : "");
                record.put("description", rs.getString("description") != null ? rs.getString("description") : "");
                record.put("price", rs.getString("price") != null ? rs.getString("price") : "");
                record.put("cleanerUid", rs.getString("cleaner_uid"));
                record.put("cleanerName",
                    (rs.getString("first_name") != null ? rs.getString("first_name") : "") + " " +
                    (rs.getString("last_name") != null ? rs.getString("last_name") : ""));
                record.put("email", rs.getString("email") != null ? rs.getString("email") : "");
            } else {
                return null;
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return record;
    }
    
}
