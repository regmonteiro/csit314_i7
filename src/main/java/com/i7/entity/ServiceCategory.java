package com.i7.entity;

import java.sql.*;


public class ServiceCategory {

    private static final String DB_URL = "jdbc:mysql://mysql-i7db.alwaysdata.net:3306/i7db_db";
    private static final String DB_USER = "i7db_admin";
    private static final String DB_PASS = "%qYyR92!N6E2";

    private int id;
    private String name;

    // Getter and Setter
    public int getId(){return id ;}
    public String getName(){return name;}

    public void setId(int id){this.id = id;}
    public void setName(String name){this.name = name;}

    // constructors
    public ServiceCategory() {}
    public ServiceCategory(int id, String name){
        this.id = id;
        this.name = name;
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

    public boolean createCategory() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO service_category (id, name) VALUES (?, ?)");
            stmt.setInt(1, generateUniqueCategoryId(conn));
            stmt.setString(2, name);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }





}
