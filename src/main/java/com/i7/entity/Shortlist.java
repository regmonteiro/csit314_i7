package com.i7.entity;

import java.sql.*;

public class Shortlist {
    private static final String DB_URL = "jdbc:mysql://mysql-i7db.alwaysdata.net:3306/i7db_db";
    private static final String DB_USER = "i7db_admin";
    private static final String DB_PASS = "%qYyR92!N6E2";

    public static boolean saveToShortlist(String uid, String cleanerId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            PreparedStatement check = conn.prepareStatement("SELECT uid FROM shortlist WHERE uid = ? AND cleaner_id = ?");
            check.setString(1, uid);
            check.setString(2, cleanerId);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                return false;
            }

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO shortlist (uid, cleaner_id) VALUES (?, ?)");
            stmt.setString(1, uid);
            stmt.setString(2, cleanerId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
