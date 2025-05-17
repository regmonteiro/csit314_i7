package com.i7.entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpSession;

public class UserAccount {
    private static final String DB_URL = "jdbc:mysql://mysql-i7db.alwaysdata.net:3306/i7db_db";
    private static final String DB_USER = "i7db_admin";
    private static final String DB_PASS = "%qYyR92!N6E2";

    private String email;
    private String password;
    private String profileCode;
    private String status;
    private String uid;
    private String firstName;
    private String lastName;

    public UserAccount() {}

    public UserAccount(String email, String password, String profileCode, String uid, String firstName, String lastName, String status) {
        this.email = email;
        this.password = password;
        this.profileCode = profileCode;
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getProfileCode() { return profileCode; }
    public String getStatus() { return status; }
    public String getUid() { return uid; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; } 

    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}
    public void setProfileCode(String profileCode) {this.profileCode = profileCode;}
    public void setStatus(String status) {this.status = status;}
    public void setUid(String uid) {this.uid = uid;}
    

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
                    rs.getString("profile_code"),
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
                return new UserAccount(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("profile_code"),
                    rs.getString("uid"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("status_code")
                );
            }
        } catch (Exception e) {
            System.out.println("Error occurred while creating user account: " + e.getMessage());
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
                    rs.getString("profile_code"),
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

    public static boolean createUserAccount(String firstName, String lastName, String email, String password, String profileCode, String statusCode) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            email = email.trim().toLowerCase();
    
            PreparedStatement check = conn.prepareStatement("SELECT email FROM user_accounts WHERE email = ?");
            check.setString(1, email);
            ResultSet rs = check.executeQuery();
            if (rs.next()) return false;
    
            String uid = generateUniqueUID(conn);
    
            if (statusCode == null || statusCode.trim().isEmpty()) {
                statusCode = "active"; // default
            }
    
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO user_accounts (email, password, profile_code, uid, first_name, last_name, status_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)");
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
    

    private static String generateUniqueUID(Connection conn) throws SQLException {
        String uid;
        boolean isUnique;

        do {
            uid = String.format("%08d", (int)(Math.random() * 1_0000_0000));
            PreparedStatement check = conn.prepareStatement("SELECT uid FROM user_accounts WHERE uid = ?");
            check.setString(1, uid);
            ResultSet rs = check.executeQuery();
            isUnique = !rs.next();
        } while (!isUnique);

        return uid;
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
                stmt.setString(3, updatedDetails.getProfileCode());
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

            return new UserAccount(
                rs.getString("email"),
                dbPassword,
                rs.getString("profile_code"),
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

    public static List<UserAccount> searchByQuery(String searchQuery) {
        List<UserAccount> results = new ArrayList<>();
        if (searchQuery == null || searchQuery.trim().isEmpty()) return results;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM user_accounts WHERE email LIKE ? OR uid LIKE ?"
            );
            String likeQuery = "%" + searchQuery.trim() + "%";
            stmt.setString(1, likeQuery);
            stmt.setString(2, likeQuery);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                results.add(getUserAccount(rs.getString("uid")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return results;
    }
}
