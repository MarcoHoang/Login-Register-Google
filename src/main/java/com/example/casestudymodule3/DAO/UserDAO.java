package com.example.casestudymodule3.DAO;

import com.example.casestudymodule3.model.User;
import java.sql.*;

public class UserDAO {

    // Kết nối với cơ sở dữ liệu
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Đảm bảo driver được nạp
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }

        String url = "jdbc:mysql://localhost:3306/user_registration?useSSL=false&serverTimezone=UTC";
        String username = "root"; // Sửa lại tùy thuộc cấu hình DB
        String password = "123456"; // Sửa lại tùy thuộc cấu hình DB

        return DriverManager.getConnection(url, username, password);
    }

    // Đăng ký người dùng
    public boolean registerUser(User user) {
        String query = "INSERT INTO users (name, email, phone, password, google_id, verification_code, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getGoogleId());
            stmt.setString(6, user.getVerificationCode());
            stmt.setString(7, user.getRole());

            return stmt.executeUpdate() > 0; // Trả về true nếu thao tác thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Đăng nhập người dùng
    public User loginUser(String email, String hashedPassword) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword); // So sánh trực tiếp hash
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Hashed Password Input: " + hashedPassword); // Debug
                System.out.println("DB Password: " + rs.getString("password")); // Debug
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getString("google_id"),
                        rs.getBoolean("is_verified"),
                        rs.getString("verification_code"),
                        rs.getString("role"),
                        rs.getString("last_login")
                );
            } else {
                System.out.println("No matching user found for email: " + email); // Debug thêm
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật trạng thái xác thực của người dùng
    public boolean verifyUser(int userId) {
        String query = "UPDATE users SET is_verified = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, true);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra email đã tồn tại trong cơ sở dữ liệu
    public boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Nếu kết quả lớn hơn 0, có email tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Email không tồn tại
    }

    // Kiểm tra số điện thoại đã tồn tại trong cơ sở dữ liệu
    public boolean isPhoneExists(String phone) {
        String query = "SELECT COUNT(*) FROM users WHERE phone = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Nếu kết quả lớn hơn 0, có số điện thoại tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Số điện thoại không tồn tại
    }
}