package com.i7.entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Listing {
    private static final String DB_URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12775162";
    private static final String DB_USER = "sql12775162";
    private static final String DB_PASS = "W653P56dDa";

    private int id; // ✅ Added ID field
    private String title;
    private String description;
    private double price;
    private String cleanerUid; // (optional for creation)

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCleanerUid() {
        return cleanerUid;
    }

    public void setCleanerUid(String cleanerUid) {
        this.cleanerUid = cleanerUid;
    }

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
    
}
