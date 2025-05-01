package com.i7.entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Listing {
    private static final String DB_URL = "jdbc:mysql://mysql-i7db.alwaysdata.net:3306/i7db_db";
    private static final String DB_USER = "i7db_admin";
    private static final String DB_PASS = "%qYyR92!N6E2";

    private int id;
    private String title;
    private String description;
    private double price;
    private String cleanerUid;

    // Getters and Setters
    public int getId() {return id;  }
    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public double getPrice() {return price;}
    public String getCleanerUid() {return cleanerUid;}

    public void setId(int id) {this.id = id;}
    public void setTitle(String title) {this.title = title;}
    public void setDescription(String description) {this.description = description;}
    public void setPrice(double price) {this.price = price;}
    public void setCleanerUid(String cleanerUid) {this.cleanerUid = cleanerUid;}

    // Constructors
    public Listing() {}

    public Listing(String title, String description, double price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    // Save Listing to DB
    public boolean saveListings() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO listings (title, description, price, cleaner_uid) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setDouble(3, price);
            stmt.setString(4, cleanerUid); // ✅ Save cleaner's UID
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Listing> fetchListings(String uid) {
        List<Listing> listings = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT id, title, description, price FROM listings WHERE cleaner_uid = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Listing listing = new Listing();
                listing.setId(rs.getInt("id"));
                listing.setTitle(rs.getString("title"));
                listing.setDescription(rs.getString("description"));
                listing.setPrice(rs.getDouble("price"));
                listings.add(listing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listings;
    }

    public static Listing getListingById(int id) {
        Listing listing = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM listings WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

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
        String sql = "UPDATE listings SET title = ?, description = ?, price = ?, cleaner_uid = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
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
    
}
