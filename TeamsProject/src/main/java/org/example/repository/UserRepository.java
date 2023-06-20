package org.example.repository;


import org.example.model.User;

import java.sql.*;

public class UserRepository {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/teamsproject";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";

    public User getUserByUsername(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            // Veritabanı bağlantısını aç
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Sorguyu hazırla
            String query = "SELECT * FROM users WHERE username = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            // Sorguyu çalıştır ve sonuçları al
            rs = stmt.executeQuery();

            // Sonuçları kullanarak User nesnesi oluştur
            if (rs.next()) {
                String fetchedUsername = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String tc = rs.getString("tc");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String userType = rs.getString("usertype");

                user = new User(fetchedUsername, password, name, surname, tc, phone, email, address, userType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Kaynakları serbest bırak
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return user;
    }
    public boolean addUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            // Veritabanı bağlantısını aç
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Sorguyu hazırla
            String query = "INSERT INTO users (username, password, name, surname, tc, phone, email, address, usertype) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getSurname());
            stmt.setString(5, user.getTc());
            stmt.setString(6, user.getPhone());
            stmt.setString(7, user.getEmail());
            stmt.setString(8, user.getAddress());
            stmt.setString(9, user.getUserType());

            // Sorguyu çalıştır ve etkilenen satır sayısını kontrol et
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Kaynakları serbest bırak
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }
}
