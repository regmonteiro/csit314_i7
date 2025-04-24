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
    private UserProfile profile;
    private String status;
    private String uid;
    private String firstName;
    private String lastName;

    public UserAccount() {}

    public UserAccount(String email, String password, UserProfile profile, String uid, String firstName, String lastName, String status) {
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public UserProfile getProfile() { return profile; }
    public String getStatus() { return status; }
    public String getUid() { return uid; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    private static UserProfile fetchProfileByCode(Connection conn, String profileCode) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_profiles WHERE code = ?");
        stmt.setString(1, profileCode);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new UserProfile(rs.getString("code"), rs.getString("name"), rs.getString("description"));
        }
        return null;
    }

    public static UserAccount findByEmail(String email) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_accounts WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserProfile profile = fetchProfileByCode(conn, rs.getString("profile_code"));
                return new UserAccount(
                    rs.getString("email"),
                    rs.getString("password"),
                    profile,
                    rs.getString("uid"),
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

    public static UserAccount getUserAccount(String uid) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_accounts WHERE uid = ?");
            stmt.setString(1, uid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserProfile profile = fetchProfileByCode(conn, rs.getString("profile_code"));
                return new UserAccount(
                    rs.getString("email"),
                    rs.getString("password"),
                    profile,
                    rs.getString("uid"),
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
                UserProfile profile = fetchProfileByCode(conn, rs.getString("profile_code"));
                users.add(new UserAccount(
                    rs.getString("email"),
                    rs.getString("password"),
                    profile,
                    rs.getString("uid"),
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
                                           String uid, String password, String profileCode, String statusCode) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement check = conn.prepareStatement("SELECT email FROM user_accounts WHERE email = ? OR uid = ?");
            email = email.trim().toLowerCase();
            uid = uid.trim();
            check.setString(1, email);
            check.setString(2, uid);
            ResultSet rs = check.executeQuery();
            if (rs.next()) return false;

            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO user_accounts (email, password, profile_code, uid, first_name, last_name, status_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, profileCode);
            stmt.setString(4, uid);
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
            updatedDetails.getProfile() != null;
    }

    public static boolean saveUpdatedDetails(String uid, UserAccount updatedDetails) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM user_accounts WHERE uid = ?");
            checkStmt.setString(1, uid);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) return false;

            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE user_accounts SET email=?, password=?, profile_code=?, first_name=?, last_name=?, status_code=? WHERE uid=?"
            );
            stmt.setString(1, updatedDetails.getEmail());
            stmt.setString(2, updatedDetails.getPassword());
            stmt.setString(3, updatedDetails.getProfile().getCode());
            stmt.setString(4, updatedDetails.getFirstName());
            stmt.setString(5, updatedDetails.getLastName());
            stmt.setString(6, updatedDetails.getStatus());
            stmt.setString(7, uid);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static UserAccount authenticateLogin(String email, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT ua.*, up.name AS profile_name, up.description AS profile_desc " +
                "FROM user_accounts ua " +
                "JOIN user_profiles up ON ua.profile_code = up.code " +
                "WHERE ua.email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
    
            if (!rs.next()) {
                return null; // Email not found
            }
    
            String dbPassword = rs.getString("password");
            if (!dbPassword.equals(password)) {
                return null; // Invalid password
            }
            
            UserProfile profile = new UserProfile(
                rs.getString("profile_code"),
                rs.getString("profile_name"),
                rs.getString("profile_desc")
            );
    
            return new UserAccount(
                rs.getString("email"),
                dbPassword,
                profile,
                rs.getString("uid"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("status_code")
            );
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

    public static boolean updateAccountStatus(String uid, String statusCode) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE user_accounts SET status_code=? WHERE uid=?");
            stmt.setString(1, statusCode);
            stmt.setString(2, uid);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
