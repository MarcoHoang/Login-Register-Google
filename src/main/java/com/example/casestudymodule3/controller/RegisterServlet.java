package com.example.casestudymodule3.controller;

import com.example.casestudymodule3.service.UserService;
import com.example.casestudymodule3.model.User;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService = new UserService();

    // Xử lý yêu cầu GET (hiển thị trang đăng ký)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/register.jsp").forward(request, response);
    }

    // Xử lý yêu cầu POST (thực hiện đăng ký người dùng)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy dữ liệu từ form đăng ký
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        // Kiểm tra nếu một trong các thông tin quan trọng bị thiếu
        if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty() || phone == null || phone.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "missing_fields");
            request.setAttribute("error_message", "Vui lòng điền đầy đủ các trường.");
            request.getRequestDispatcher("view/register.jsp").forward(request, response);
            return;
        }

        // Tạo đối tượng User
        User user = new User(0, name, email, phone, password, null, false, null, "USER", null);

        try {
            // Đăng ký người dùng qua UserService
            boolean result = userService.registerUser(user);

            if (result) {
                // Đăng ký thành công, chuyển hướng đến trang đăng nhập
                response.sendRedirect(request.getContextPath() + "/view/login.jsp");
            } else {
                // Đăng ký thất bại (lỗi không xác định từ UserService)
                request.setAttribute("error", "unknown_error");
                request.setAttribute("error_message", "Đăng ký thất bại. Vui lòng thử lại.");
                request.getRequestDispatcher("view/register.jsp").forward(request, response);
            }
        } catch (IllegalArgumentException e) {
            // Xử lý các ngoại lệ từ UserService (email không hợp lệ, email/số điện thoại đã tồn tại)
            String errorMessage;
            if (e.getMessage().equals("Invalid email format.")) {
                errorMessage = "Email không hợp lệ.";
            } else if (e.getMessage().equals("Invalid phone number format.")) {
                errorMessage = "Số điện thoại không hợp lệ.";
            } else if (e.getMessage().equals("Email already exists.")) {
                errorMessage = "Email đã được đăng ký.";
            } else if (e.getMessage().equals("Phone number already exists.")) {
                errorMessage = "Số điện thoại đã được đăng ký.";
            } else {
                errorMessage = "Lỗi không xác định: " + e.getMessage();
            }

            request.setAttribute("error", "validation_error");
            request.setAttribute("error_message", errorMessage);
            request.getRequestDispatcher("view/register.jsp").forward(request, response);
        } catch (Exception e) {
            // Xử lý các lỗi khác (lỗi hệ thống, lỗi cơ sở dữ liệu, v.v.)
            request.setAttribute("error", "system_error");
            request.setAttribute("error_message", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("view/register.jsp").forward(request, response);
        }
    }
}