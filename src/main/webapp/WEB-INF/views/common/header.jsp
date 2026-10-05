<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.aashwin.aashwinmart.util.CsrfUtil" %>
<% CsrfUtil.getOrCreateToken(request); %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'AASHWIN MART — Premium Marketplace'}" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <%-- Expose CSRF token to JavaScript (safe - not a secret, bound to session) --%>
    <meta name="csrf-token" content="${sessionScope._csrfToken}">
</head>
<body>

<!-- Top Notification Bar -->
<div class="nav-topbar">
    <div class="container">
        <span>✨ Welcome to AASHWIN MART — Royal Shopping. Trusted Marketplace.</span>
        <span>👑 100% Verified Luxury Collections &amp; Free Royal Shipping</span>
    </div>
</div>

<!-- Header Navigation -->
<header class="header-nav">
    <div class="nav-main">
        <div class="container">
            <div class="nav-wrapper">
                <!-- Brand Identity -->
                <a href="${pageContext.request.contextPath}/" class="brand-logo">
                    <svg class="crown-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="m2 4 3 12h14l3-12-6 7-4-7-4 7-6-7zm3 16h14"/>
                    </svg>
                    <div class="brand-text">
                        <span class="brand-title">AASHWIN <span>MART</span></span>
                        <span class="brand-subtitle">Royal Shopping</span>
                    </div>
                </a>

                <!-- Keyword Search Form -->
                <div class="nav-search">
                    <form action="${pageContext.request.contextPath}/shop" method="GET" class="search-form">
                        <svg class="search-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/>
                        </svg>
                        <input type="text" name="q" value="<c:out value='${param.q}'/>" placeholder="Search luxury products, timepieces, fashion..." class="search-input">
                    </form>
                </div>

                <!-- Navigation Links & User Role Menus -->
                <nav>
                    <ul class="nav-links">
                        <li><a href="${pageContext.request.contextPath}/" class="nav-link">Home</a></li>
                        <li><a href="${pageContext.request.contextPath}/shop" class="nav-link">Shop</a></li>

                        <c:choose>
                            <c:when test="${sessionScope.currentUser != null}">
                                <c:choose>
                                    <c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
                                        <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link text-gold">Admin Panel</a></li>
                                        <li><a href="${pageContext.request.contextPath}/admin/users" class="nav-link">Users</a></li>
                                        <li><a href="${pageContext.request.contextPath}/admin/orders" class="nav-link">Orders</a></li>
                                        <li><a href="${pageContext.request.contextPath}/admin/products" class="nav-link">Listings</a></li>
                                    </c:when>
                                    <c:when test="${sessionScope.currentUser.role == 'SELLER'}">
                                        <li><a href="${pageContext.request.contextPath}/seller/dashboard" class="nav-link text-gold">Dashboard</a></li>
                                        <li><a href="${pageContext.request.contextPath}/seller/products" class="nav-link">My Products</a></li>
                                        <li><a href="${pageContext.request.contextPath}/seller/orders" class="nav-link">Orders</a></li>
                                    </c:when>
                                    <c:otherwise>
                                        <li><a href="${pageContext.request.contextPath}/buyer/dashboard" class="nav-link">Dashboard</a></li>
                                        <li><a href="${pageContext.request.contextPath}/orders" class="nav-link">My Orders</a></li>
                                    </c:otherwise>
                                </c:choose>

                                <!-- Cart Icon for Buyers & Visitors -->
                                <c:if test="${sessionScope.currentUser.role != 'ADMIN' && sessionScope.currentUser.role != 'SELLER'}">
                                    <li>
                                        <a href="${pageContext.request.contextPath}/cart" class="cart-btn" title="View Cart">
                                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                                <circle cx="8" cy="21" r="1"/><circle cx="19" cy="21" r="1"/>
                                                <path d="M2.05 2.05h2l2.66 12.42a2 2 0 0 0 2 1.58h9.78a2 2 0 0 0 1.95-1.57l1.65-7.43H5.12"/>
                                            </svg>
                                            <span class="cart-badge js-cart-badge">${cartCount != null ? cartCount : 0}</span>
                                        </a>
                                    </li>
                                </c:if>

                                <li>
                                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-sm">Logout (<c:out value="${sessionScope.currentUser.name}"/>)</a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="${pageContext.request.contextPath}/register-seller" class="nav-link">Become a Seller</a></li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/cart" class="cart-btn" title="View Cart">
                                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                            <circle cx="8" cy="21" r="1"/><circle cx="19" cy="21" r="1"/>
                                            <path d="M2.05 2.05h2l2.66 12.42a2 2 0 0 0 2 1.58h9.78a2 2 0 0 0 1.95-1.57l1.65-7.43H5.12"/>
                                        </svg>
                                        <span class="cart-badge js-cart-badge">0</span>
                                    </a>
                                </li>
                                <li><a href="${pageContext.request.contextPath}/login" class="nav-link">Login</a></li>
                                <li><a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm">Join Royal</a></li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</header>
<main>
