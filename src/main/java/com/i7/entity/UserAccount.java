package com.i7.entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpSession;

public class UserAccount {
    private static final String DB_URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12775162";
    private static final String DB_USER = "sql12775162";
    private static final String DB_PASS = "W653P56dDa";

    private String email;
    private String password;
    private String role;
    private String status; 
    private String username;
    private String firstName;
    private String lastName;

    public UserAccount() {}

    public UserAccount(String email, String password, String role, String username, String firstName, String lastName, String status) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

    // Getters
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public static UserAccount findByEmail(String email) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_accounts WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserAccount(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role_code"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("status_code")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserAccount getUserAccount(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_accounts WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserAccount(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role_code"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("status_code")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UserAccount> getAllUserAccounts() {
        List<UserAccount> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_accounts");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new UserAccount(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role_code"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("status_code")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public static boolean createNewAccount(String firstName, String lastName, String email,
                                           String username, String password, String roleCode, String statusCode) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement check = conn.prepareStatement("SELECT email FROM user_accounts WHERE email = ? OR username = ?");
            email = email.trim().toLowerCase();
            username = username.trim();
            check.setString(1, email);
            check.setString(2, username);
            ResultSet rs = check.executeQuery();
            if (rs.next()) return false;

            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO user_accounts (email, password, role_code, username, first_name, last_name, status_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, roleCode);
            stmt.setString(4, username);
            stmt.setString(5, firstName);
            stmt.setString(6, lastName);
            stmt.setString(7, statusCode);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateDetails(UserAccount updatedDetails) {
        return updatedDetails != null &&
            updatedDetails.getEmail() != null && !updatedDetails.getEmail().isEmpty() &&
            updatedDetails.getFirstName() != null && !updatedDetails.getFirstName().isEmpty() &&
            updatedDetails.getLastName() != null && !updatedDetails.getLastName().isEmpty() &&
            updatedDetails.getPassword() != null && !updatedDetails.getPassword().isEmpty() &&
            updatedDetails.getRole() != null && !updatedDetails.getRole().isEmpty();
    }

    public static boolean saveUpdatedDetails(String userId, UserAccount updatedDetails) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM user_accounts WHERE username = ?");
            checkStmt.setString(1, userId);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) return false;

            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE user_accounts SET email=?, password=?, role_code=?, first_name=?, last_name=?, status_code=? WHERE username=?");
            stmt.setString(1, updatedDetails.getEmail());
            stmt.setString(2, updatedDetails.getPassword());
            stmt.setString(3, updatedDetails.getRole());
            stmt.setString(4, updatedDetails.getFirstName());
            stmt.setString(5, updatedDetails.getLastName());
            stmt.setString(6, updatedDetails.getStatus());
            stmt.setString(7, userId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static UserAccount authenticateLogin(String email, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_accounts WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                String status = rs.getString("status_code");
                if (!dbPassword.equals(password) || status.equalsIgnoreCase("suspended")) {
                    return null;
                }

                return new UserAccount(
                    rs.getString("email"),
                    dbPassword,
                    rs.getString("role_code"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    status
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean logout(HttpSession session) {
        try {
            session.invalidate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateAccountStatus(String username, String statusCode) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE user_accounts SET status_code=? WHERE username=?");
            stmt.setString(1, statusCode);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
