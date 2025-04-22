package com.i7.entity;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Role {
    private String code;
    private String name;

    public Role(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() { return code; }
    public String getName() { return name; }

    public static List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/i7_db", "root", "1234")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM roles");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                roles.add(new Role(rs.getString("code"), rs.getString("name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roles;
    }
}
