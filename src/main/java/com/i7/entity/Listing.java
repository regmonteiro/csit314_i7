package com.i7.entity;

import java.sql.*;

public class Listing {
    private static final String DB_URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12775162";
    private static final String DB_USER = "sql12775162";
    private static final String DB_PASS = "W653P56dDa";

    private String title;
    private String description;
    private double price;

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

    public Listing() {}

    public Listing(String title, String description, double price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public boolean saveListings() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO listings (title, description, price) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setDouble(3, price);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
