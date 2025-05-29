<%--
  Created by IntelliJ IDEA.
  User: Hoang Tran
  Date: 28/05/2025
  Time: 22:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet">
</head>
<body>
<div class="card-container">
    <h2 id="loginTitle">Chào mừng trở lại</h2>
    <!-- Hiển thị thông báo lỗi nếu có -->
    <c:if test="${not empty error_message}">
        <div class="error-message show">
                ${error_message}
        </div>
    </c:if>
    <!-- Form nhập email -->
    <form id="emailForm" style="display: ${empty sessionScope.email ? 'block' : 'none'}">
        <div class="form-group">
            <input type="email" class="form-control" id="email" name="email" placeholder="Email" value="${param.email != null ? param.email : ''}" required>
            <div id="emailError" class="error-message">
                Vui lòng nhập địa chỉ email hợp lệ.
            </div>
        </div>
        <button type="button" class="btn btn-primary" onclick="checkEmail()">Đăng nhập</button>
    </form>
    <!-- Form nhập mật khẩu -->
    <form id="passwordForm" action="${pageContext.request.contextPath}/login" method="post" style="display: ${not empty sessionScope.email ? 'block' : 'none'}">
        <input type="hidden" name="email" value="${sessionScope.email}">
        <div class="form-group">
            <input type="password" class="form-control" id="password" name="password" placeholder="Mật khẩu" required>
            <div id="passwordError" class="error-message">
                Mật khẩu không đúng.
            </div>
        </div>
        <button type="submit" class="btn btn-primary">Đăng nhập</button>
        <button type="button" class="btn btn-link mt-2" onclick="goBackToEmail()">Quay lại</button>
    </form>
    <p class="text-center mt-3">Bạn chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Hãy đăng ký</a></p>
    <div class="divider"><span>HOẶC</span></div>
    <a href="GOOGLE_AUTH_URL" class="btn btn-google">
        <img src="https://developers.google.com/identity/images/g-logo.png" alt="Google Icon" class="google-icon">
        Tiếp tục với Google
    </a>
    <p class="text-center mt-4">
        <a href="#">Điều khoản sử dụng</a> | <a href="#">Chính sách riêng tư</a>
    </p>
</div>
<script>
    function checkEmail() {
        const email = document.getElementById('email').value;
        const emailError = document.getElementById('emailError');
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailRegex.test(email)) {
            document.getElementById('email').classList.add('error');
            emailError.classList.add('show');
            return;
        }

        // Gửi AJAX để kiểm tra email tồn tại
        fetch('${pageContext.request.contextPath}/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'email=' + encodeURIComponent(email)
        })
            .then(response => response.json())
            .then(data => {
                if (data.exists) {
                    // Email tồn tại, lưu vào session
                    const xhr = new XMLHttpRequest();
                    xhr.open('GET', '${pageContext.request.contextPath}/login?setEmail=' + encodeURIComponent(email), true);
                    xhr.onreadystatechange = function() {
                        if (xhr.readyState === 4 && xhr.status === 200) {
                            document.getElementById('emailForm').style.display = 'none';
                            document.getElementById('passwordForm').style.display = 'block';
                            document.getElementById('loginTitle').innerText = 'Nhập mật khẩu';
                        }
                    };
                    xhr.send();
                } else {
                    // Email không tồn tại
                    document.getElementById('email').classList.add('error');
                    emailError.innerText = 'Email không tồn tại.';
                    emailError.classList.add('show');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById('email').classList.add('error');
                emailError.innerText = 'Lỗi hệ thống. Vui lòng thử lại.';
                emailError.classList.add('show');
            });
    }

    function goBackToEmail() {
        // Quay lại bước nhập email
        fetch('${pageContext.request.contextPath}/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'resetEmail=true'
        })
            .then(() => {
                document.getElementById('passwordForm').style.display = 'none';
                document.getElementById('emailForm').style.display = 'block';
                document.getElementById('loginTitle').innerText = 'Chào mừng trở lại';
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>