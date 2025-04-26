// ignore this first might be wrong

package com.i7.controller;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RoleController {
    private static final String DB_URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12775162";
    private static final String DB_USER = "sql12775162";
    private static final String DB_PASS = "W653P56dDa";

    private String code;
    private String name;
    private String description;

    public RoleController() {}

    public RoleController(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    public static List<String> getAllowedProfilesForSignup() {
        return List.of("homeowner", "cleaner");
    }

    public static Role findByCode(String code) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM roles WHERE code = ?");
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Role(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getString("description")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM roles");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                roles.add(new Role(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getString("description")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roles;
    }

    public static boolean createRole(String code, String name, String description) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO roles (code, name, description) VALUES (?, ?, ?)");
            stmt.setString(1, code);
            stmt.setString(2, name);
            stmt.setString(3, description);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateRole(String code, String newName, String newDescription) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE roles SET name = ?, description = ? WHERE code = ?");
            stmt.setString(1, newName);
            stmt.setString(2, newDescription);
            stmt.setString(3, code);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteRole(String code) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM roles WHERE code = ?");
            stmt.setString(1, code);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}