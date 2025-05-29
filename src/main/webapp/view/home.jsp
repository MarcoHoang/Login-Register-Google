<%--
  Created by IntelliJ IDEA.
  User: Hoang Tran
  Date: 28/05/2025
  Time: 22:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Trang chủ</title>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/lib/bootstrap.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
<div class="container">
  <h2>Chào mừng bạn đến với trang chủ!</h2>
  <c:if test="${not empty user}">
    <div class="card">
      <div class="card-body">
        <h5 class="card-title">Thông tin người dùng</h5>
        <p class="card-text"><strong>Họ và tên:</strong> ${user.name}</p>
        <p class="card-text"><strong>Email:</strong> ${user.email}</p>
        <p class="card-text"><strong>Số điện thoại:</strong> ${user.phone}</p>
        <p class="card-text"><strong>Vai trò:</strong> ${user.role}</p>
        <c:if test="${user.googleId != null}">
          <p class="card-text"><strong>Đăng nhập qua Google:</strong> Có</p>
        </c:if>
      </div>
    </div>
    <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger w-100 mt-3">Đăng xuất</a>
  </c:if>
  <c:if test="${empty user}">
    <p class="text-center">Vui lòng <a href="${pageContext.request.contextPath}/login">đăng nhập</a> để xem thông tin.</p>
  </c:if>
</div>
<script src="${pageContext.request.contextPath}/lib/bootstrap.bundle.min.js"></script>
</body>
</html>
