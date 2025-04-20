package com.i7.entity;

import java.sql.*;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;

public class UserAccount {
    private String email;
    private String password;
    private String role;
    private String username;
    private String firstName;
    private String lastName;

    public UserAccount(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = "";
        this.firstName = "";
        this.lastName = "";
    }

    public UserAccount(String email, String password, String role, String username) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = username;
        this.firstName = "";
        this.lastName = "";
    }

    public UserAccount(String email, String password, String role, String username, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

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
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/i7_db", "root", "1234")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_accounts WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserAccount(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UserAccount> getAllUserAccounts() {
        List<UserAccount> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/i7_db", "root", "1234")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_accounts");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserAccount user = new UserAccount(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
      
    return users;
}
    
    public static boolean createNewAccount(String firstName, String lastName, String email, String username, String password, String role) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/webapp_db", "webuser", "password123")) {
            PreparedStatement check = conn.prepareStatement("SELECT email FROM user_accounts WHERE email = ?");
            check.setString(1, email);
            ResultSet rs = check.executeQuery();
            if (rs.next()) return false;

            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO user_accounts (email, password, role, username, first_name, last_name) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.setString(4, username);
            stmt.setString(5, firstName);
            stmt.setString(6, lastName);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static UserAccount authenticateLogin(String email, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/i7_db", "root", "1234")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_accounts WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                String role = rs.getString("role");
    
                // Check for password match and suspension status
                if (!dbPassword.equals(password) || role.equalsIgnoreCase("suspended")) {
                    return null;
                }
    
                return new UserAccount(
                    rs.getString("email"),
                    dbPassword,
                    role,
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return null;
    }
    
    public static boolean logout(HttpSession session) {
    try {
        session.invalidate();  // Destroy session
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

}