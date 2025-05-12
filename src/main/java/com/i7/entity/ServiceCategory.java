package com.i7.entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategory {

    private static final String DB_URL = "jdbc:mysql://mysql-i7db.alwaysdata.net:3306/i7db_db";
    private static final String DB_USER = "i7db_admin";
    private static final String DB_PASS = "%qYyR92!N6E2";

    private int id;
    private String name;
    private String description;

    // Getter and Setter
    public int getId(){return id ;}
    public String getName(){return name;}
    public String getDescription(){return description;}


    public void setId(int id){this.id = id;}
    public void setName(String name){this.name = name;}
    public void setDescription(String description){this.description=description;}

    // constructors
    public ServiceCategory() {}
    public ServiceCategory(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description=description;
    }

    private static int generateUniqueCategoryId(Connection conn) throws SQLException {
        int id = 1;
        boolean exists;
        String sql = "SELECT id FROM service_category WHERE id = ?";
        try (PreparedStatement check = conn.prepareStatement(sql)) {
            check.setInt(1, id);
            try (ResultSet rs = check.executeQuery()) {
                exists = rs.next();
            }
        }
        if (exists) {
            id++;
        }while (exists);
        return id;
        } 

    public static boolean createCategory(int id, String name, String description) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO service_category (id, name, description) VALUES (?, ?, ?)");
            stmt.setInt(1, generateUniqueCategoryId(conn));
            stmt.setString(2, name);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static ServiceCategory getCategoryByName(String name) {
        ServiceCategory category = new ServiceCategory();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM service_category WHERE name = ?");
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                category = new ServiceCategory();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    public static List<ServiceCategory> fetchCategories (){
        List<ServiceCategory> categories = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)){PreparedStatement stmt = conn.prepareStatement(
            "SELECT * FROM service_categories");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categories.add(new ServiceCategory(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }
    

    public static boolean updateCategory(String name, ServiceCategory updatedCategory) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM service_category WHERE name = ?")) {
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) return false;
    
                PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE service_categories SET name = ?, description = ?"
                );
                stmt.setString(1, updatedCategory.getName());
                stmt.setString(2, updatedCategory.getDescription());
                return stmt.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
    }
    public static boolean deleteCategory(String name){
        ServiceCategory category = new ServiceCategory();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM service_category WHERE name = ?")) {
            stmt.setString(1, category.getName());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<ServiceCategory> searchByQuery(String searchQuery) {
        List<ServiceCategory> results = new ArrayList<>();
        if (searchQuery == null || searchQuery.trim().isEmpty()) return results;
    
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM service_category WHERE name LIKE ? OR description LIKE ?"
            );
            String likeQuery = "%" + searchQuery.trim() + "%";
            stmt.setString(1, likeQuery);
            stmt.setString(2, likeQuery);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                results.add(getCategoryByName(rs.getString("name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return results;
    }
}




