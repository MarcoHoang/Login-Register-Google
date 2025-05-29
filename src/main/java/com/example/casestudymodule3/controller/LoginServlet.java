package com.example.casestudymodule3.controller;

import com.example.casestudymodule3.service.UserService;
import com.example.casestudymodule3.model.User;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String setEmail = request.getParameter("setEmail");
        if (setEmail != null) {
            // Lưu email vào session để chuyển sang bước nhập mật khẩu
            request.getSession().setAttribute("email", setEmail);
            response.sendRedirect(request.getContextPath() + "/view/login.jsp");
            return;
        }
        request.getRequestDispatcher("/view/login.jsp").forward(request, response); // Chuyển hướng đến trang đăng nhập
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String resetEmail = request.getParameter("resetEmail");

        // Xử lý reset email để quay lại bước nhập email
        if ("true".equals(resetEmail)) {
            session.removeAttribute("email");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"success\": true}");
            out.flush();
            return;
        }

        // Bước 1: Kiểm tra email (AJAX)
        if (password == null) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            boolean exists = userService.isEmailExists(email);
            out.print("{\"exists\":" + exists + "}");
            out.flush();
            return;
        }

        // Bước 2: Kiểm tra đăng nhập hoàn chỉnh
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error_message", "Vui lòng điền đầy đủ email và mật khẩu.");
            session.removeAttribute("email"); // Reset email khi lỗi
            request.getRequestDispatcher("/view/login.jsp").forward(request, response);
            return;
        }

        // Mã hóa mật khẩu một lần duy nhất
        String hashedPassword = userService.hashPassword(password);

        // Kiểm tra đăng nhập
        User user = userService.loginUser(email, hashedPassword);
        if (user != null) {
            // Đăng nhập thành công, lưu user vào session
            session.setAttribute("user", user);
            session.removeAttribute("email"); // Xóa email tạm thời
            response.sendRedirect(request.getContextPath() + "/view/home.jsp"); // Chuyển hướng đến trang home
        } else {
            // Đăng nhập thất bại, reset về bước email
            request.setAttribute("error_message", "Email hoặc mật khẩu không đúng.");
            session.removeAttribute("email"); // Xóa email để quay lại bước email
            request.getRequestDispatcher("/view/login.jsp").forward(request, response);
        }
    }
}