package com.i7.entity;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class UserAccount {
    private String email;
    private String password;
    private String role;
    private String username;

    public UserAccount(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UserAccount(String email, String password, String role, String username) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = username;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getUsername() {return username;}

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
                    rs.getString("role"),
                    rs.getString("username")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

  public static UserAccount getUserAccount(String username) {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/i7_db", "root", "1234");

            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM user_accounts WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new UserAccount(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("username")
                );
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UserAccount> getAllUserAccounts() {
    List<UserAccount> users = new ArrayList<>();
    try {
        Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/i7_db", "root", "1234");

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_accounts");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            UserAccount user = new UserAccount(
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("username")
            );
            users.add(user);
        }

        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return users;
}
    

    public static boolean createNewAccount(String firstName, String lastName, String email,
                                       String username, String password, String role) {
    try {
        Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/webapp_db", "webuser", "password123");

        // Check if email or username already exists
        PreparedStatement check = conn.prepareStatement("SELECT email FROM user_accounts WHERE email = ?");
        check.setString(1, email);
        ResultSet rs = check.executeQuery();
        if (rs.next()) return false;

        // Insert new user
        PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO user_accounts (email, password, role, username) VALUES (?, ?, ?, ?)");
        stmt.setString(1, email);
        stmt.setString(2, password);
        stmt.setString(3, role);
        stmt.setString(4, username);
        int rows = stmt.executeUpdate();

        return rows > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

}