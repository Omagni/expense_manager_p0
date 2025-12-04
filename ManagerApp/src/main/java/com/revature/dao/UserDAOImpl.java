package com.revature.dao;

import com.revature.model.User;
import java.sql.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserDAOImpl implements UserDAO {

    // SQLite DB path setup
    private static final String DB_URL;
    static {
        String baseDir = System.getProperty("user.dir");
        Path dbPath = Paths.get(baseDir, "..", "revature_expense.db").toAbsolutePath().normalize();
        DB_URL = "jdbc:sqlite:" + dbPath.toString();
    }

    @Override
    public User findUserByUsername(String username) {
        String sql = "SELECT id, username, password, role FROM user_info WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs.getInt("id"), rs.getString("username"),
                            rs.getString("password"), rs.getString("role") );
                    return user;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }

        return null; // user not found
    }
}
