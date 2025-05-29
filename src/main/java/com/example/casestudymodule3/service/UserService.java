package com.example.casestudymodule3.service;

import com.example.casestudymodule3.DAO.UserDAO;
import com.example.casestudymodule3.model.User;
import com.example.casestudymodule3.util.Validator;  // Import lớp Validator

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    // Mã hóa mật khẩu người dùng
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tạo mã xác minh ngẫu nhiên
    public String generateVerificationCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // Kiểm tra email đã tồn tại trong cơ sở dữ liệu
    public boolean isEmailExists(String email) {
        return userDAO.isEmailExists(email);
    }

    // Kiểm tra số điện thoại đã tồn tại trong cơ sở dữ liệu
    public boolean isPhoneExists(String phone) {
        return userDAO.isPhoneExists(phone);
    }

    // Đăng ký người dùng
    public boolean registerUser(User user) {
        // Validate email và phone
        if (!Validator.isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (!Validator.isValidPhone(user.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }
        if (isEmailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }
        if (isPhoneExists(user.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists.");
        }

        // Mã hóa mật khẩu và tạo mã xác minh
        user.setPassword(hashPassword(user.getPassword()));
        user.setVerificationCode(generateVerificationCode());

        return userDAO.registerUser(user);
    }

    // Đăng nhập người dùng
    public User loginUser(String email, String hashedPassword) {
        return userDAO.loginUser(email, hashedPassword); // Gọi phương thức loginUser trong UserDAO
    }

    // Xác thực người dùng
    public boolean verifyUser(int userId) {
        return userDAO.verifyUser(userId);
    }
}
