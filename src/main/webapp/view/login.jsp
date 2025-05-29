<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet">
</head>
<body>
<div class="card-container">
    <h2 id="loginTitle">${empty sessionScope.email ? "Chào mừng trở lại" : "Nhập mật khẩu"}</h2>

    <!-- Thông báo lỗi nếu có -->
    <c:if test="${not empty error_message}">
        <div class="error-message show">
                ${error_message}
        </div>
    </c:if>

    <!-- Form nhập email -->
    <c:if test="${empty sessionScope.email}">
        <form action="${pageContext.request.contextPath}/login" method="get">
            <div class="form-group">
                <input type="email" class="form-control" name="setEmail" placeholder="Email" required>
            </div>
            <button type="submit" class="btn btn-primary">Tiếp tục</button>
        </form>
    </c:if>

    <!-- Form nhập mật khẩu -->
    <c:if test="${not empty sessionScope.email}">
        <form action="${pageContext.request.contextPath}/login" method="post">
            <input type="hidden" name="email" value="${sessionScope.email}">
            <div class="form-group">
                <input type="password" class="form-control" name="password" placeholder="Mật khẩu" required>
            </div>
            <button type="submit" class="btn btn-primary">Đăng nhập</button>
        </form>
        <form action="${pageContext.request.contextPath}/login" method="post" class="mt-2">
            <input type="hidden" name="resetEmail" value="true">
            <button type="submit" class="btn btn-link">Quay lại</button>
        </form>
    </c:if>

    <p class="text-center mt-3">
        Bạn chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Hãy đăng ký</a>
    </p>
    <div class="divider"><span>HOẶC</span></div>
    <a href="GOOGLE_AUTH_URL" class="btn btn-google">
        <img src="https://developers.google.com/identity/images/g-logo.png" alt="Google Icon" class="google-icon">
        Tiếp tục với Google
    </a>
    <p class="text-center mt-4">
        <a href="#">Điều khoản sử dụng</a> | <a href="#">Chính sách riêng tư</a>
    </p>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
