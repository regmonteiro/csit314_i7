package com.i7.entity;

import java.sql.*;

public class UserAccount {
    private String email;
    private String password;
    private String role;

    public UserAccount(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public static UserAccount findByEmail(String email) {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/i7_db", "root", "1234");
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM user_accounts WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserAccount(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean createNewAccount(String firstName, String lastName, String email,
                                       String username, String password, String role) {
    try {
        Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/webapp_db", "webuser", "password123");

        // Optional: check if email or username already exists
        PreparedStatement check = conn.prepareStatement("SELECT email FROM user_accounts WHERE email = ?");
        check.setString(1, email);
        ResultSet rs = check.executeQuery();
        if (rs.next()) return false;

        // Insert new user
        PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO user_accounts (email, password, role) VALUES (?, ?, ?)");
        stmt.setString(1, email);
        stmt.setString(2, password);
        stmt.setString(3, role);
        int rows = stmt.executeUpdate();

        return rows > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

}