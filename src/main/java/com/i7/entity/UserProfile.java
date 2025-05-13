package com.i7.entity;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfile {
    private static final String DB_URL = "jdbc:mysql://mysql-i7db.alwaysdata.net:3306/i7db_db";
    private static final String DB_USER = "i7db_admin";
    private static final String DB_PASS = "%qYyR92!N6E2";

    private String code;
    private String name;
    private String description;
    private String status_code;

    public UserProfile() {}

    public UserProfile(String code, String name, String description, String status_code) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.status_code = status_code;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getStatus() {return status_code;}

    public void setCode(String code) { this.code = code; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description;}
    public void setStatus(String status_code) {this.status_code = status_code;}

    public static UserProfile findByCode(String code) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_profiles WHERE code = ?");
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new UserProfile(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("status_code")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }   
    
    public static List<UserProfile> findProfilesByQuery(String searchQuery) {
        List<UserProfile> profiles = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // Modified SQL query to search by both name and code
            String sql = "SELECT * FROM user_profiles WHERE name LIKE ? OR code LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            // Search for both name and code using the search query
            String searchPattern = "%" + searchQuery + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern); 
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                profiles.add(new UserProfile(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("status_code")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return profiles;
    }    
    public static List<UserProfile> getAllProfiles() {
        List<UserProfile> profiles = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_profiles");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                profiles.add(new UserProfile(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("status_code")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return profiles;
    }

    public static boolean createProfile(String name, String description) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            
            PreparedStatement check = conn.prepareStatement("SELECT name FROM user_profiles WHERE name = ?");
            check.setString(1, name);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                return false;
            }
    
            String code = generateUniqueProfileCode(conn);
    
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO user_profiles (code, name, description, status_code) VALUES (?, ?, ?, ?)"
            );
            stmt.setString(1, code);
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.setString(4, "active"); // Default status_code
    
            return stmt.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static String generateUniqueProfileCode(Connection conn) throws SQLException {
        String code;
        boolean isUnique;
    
        do {
            int random = (int) (Math.random() * 900) + 100;
            code = "P" + random;
            PreparedStatement stmt = conn.prepareStatement("SELECT code FROM user_profiles WHERE code = ?");
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            isUnique = !rs.next();
        } while (!isUnique);
    
        return code;
    }
    
    public static boolean updateProfile(String code, String newName, String newDescription) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE user_profiles SET name = ?, description = ? WHERE code = ?"
            );
            stmt.setString(1, newName);
            stmt.setString(2, newDescription);
            stmt.setString(3, code);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }    

    public static boolean updateProfileStatus(String profileCode, String status_code) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "UPDATE user_profiles SET status_code = ? WHERE code = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, status_code);
            stmt.setString(2, profileCode);
    
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


