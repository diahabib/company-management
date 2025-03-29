<%--
  Created by IntelliJ IDEA.
  User: habib
  Date: 29/03/2025
  Time: 13:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>${pageTitle != null ? pageTitle : 'Gestion Entreprise'}</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <style>
    :root {
      --primary-color: #4361ee;
      --secondary-color: #3f37c9;
      --accent-color: #4895ef;
      --danger-color: #f72585;
      --light-color: #f8f9fa;
      --dark-color: #212529;
    }

    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      margin: 0;
      padding: 0;
      background-color: #f5f7fa;
    }

    .app-container {
      display: grid;
      grid-template-columns: 250px 1fr;
      min-height: 100vh;
    }

    /* Sidebar styles */
    .sidebar {
      background-color: var(--dark-color);
      color: white;
      padding: 1.5rem 0;
      box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
    }

    .sidebar-header {
      padding: 0 1.5rem 1.5rem;
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    }

    .sidebar-nav {
      margin-top: 1rem;
    }

    .nav-item {
      display: flex;
      align-items: center;
      padding: 0.75rem 1.5rem;
      color: rgba(255, 255, 255, 0.8);
      text-decoration: none;
      transition: all 0.3s;
    }

    .nav-item:hover, .nav-item.active {
      background-color: rgba(255, 255, 255, 0.1);
      color: white;
    }

    .nav-item i {
      margin-right: 0.75rem;
      width: 20px;
      text-align: center;
    }

    /* Main content styles */
    .main-content {
      padding: 2rem;
    }

    .top-bar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 2rem;
      padding-bottom: 1rem;
      border-bottom: 1px solid #e0e0e0;
    }

    .user-menu {
      display: flex;
      align-items: center;
    }

    .user-avatar {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      background-color: var(--primary-color);
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 0.75rem;
      font-weight: bold;
    }

    .logout-btn {
      background: none;
      border: none;
      color: var(--danger-color);
      cursor: pointer;
      display: flex;
      align-items: center;
    }

    .logout-btn i {
      margin-right: 0.5rem;
    }

    /* Card styles */
    .card {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
      padding: 1.5rem;
      margin-bottom: 1.5rem;
    }

    .card-title {
      margin-top: 0;
      color: var(--dark-color);
      border-bottom: 1px solid #eee;
      padding-bottom: 0.75rem;
    }
  </style>
</head>
<body>
<div class="app-container">
  <!-- Sidebar -->
  <aside class="sidebar">
    <div class="sidebar-header">
      <h2 style="margin: 0; display: flex; align-items: center;">
        <i class="fas fa-building" style="margin-right: 0.5rem;"></i>
        <span>Entreprise</span>
      </h2>
      <p style="margin: 0.5rem 0 0; font-size: 0.9rem; opacity: 0.8;">
        Gestion intégrée
      </p>
    </div>

    <nav class="sidebar-nav">
      <c:if test="${userService.hasRole(user, 'ADMIN')}">
        <a href="${pageContext.request.contextPath}/employees" class="nav-item ${activePage == 'employees' ? 'active' : ''}">
          <i class="fas fa-users"></i>
          <span>Employés</span>
        </a>
        <a href="${pageContext.request.contextPath}/departments" class="nav-item ${activePage == 'departments' ? 'active' : ''}">
          <i class="fas fa-sitemap"></i>
          <span>Départements</span>
        </a>
      </c:if>

      <c:if test="${userService.hasRole(user, 'MANAGER')}">
        <a href="${pageContext.request.contextPath}/projects" class="nav-item ${activePage == 'projects' ? 'active' : ''}">
          <i class="fas fa-project-diagram"></i>
          <span>Projets</span>
        </a>
      </c:if>

      <a href="${pageContext.request.contextPath}/tasks" class="nav-item ${activePage == 'tasks' ? 'active' : ''}">
        <i class="fas fa-tasks"></i>
        <span>Tâches</span>
      </a>

      <a href="${pageContext.request.contextPath}/employees" class="nav-item ${activePage == 'employees' ? 'active' : ''}">
        <i class="fas fa-tasks"></i>
        <span>Employés</span>
      </a>

      <a href="${pageContext.request.contextPath}/
      projects" class="nav-item ${activePage == 'projects' ? 'active' : ''}">
        <i class="fas fa-tasks"></i>
        <span>Projets</span>
      </a>

      <a href="${pageContext.request.contextPath}/departments" class="nav-item ${activePage == 'departments' ? 'active' : ''}">
        <i class="fas fa-tasks"></i>
        <span>Departements</span>
      </a>

    </nav>
  </aside>

  <!-- Main Content -->
  <main class="main-content">
    <header class="top-bar">
      <h1>${pageTitle != null ? pageTitle : 'Tableau de bord'}</h1>

      <div class="user-menu">
        <div class="user-avatar">
          ${fn:substring(user.username, 0, 1)}
        </div>
        <span style="margin-right: 1rem;">${user.username}</span>
        <form action="${pageContext.request.contextPath}/logout" method="post">
          <button type="submit" class="logout-btn">
            <i class="fas fa-sign-out-alt"></i>
            Déconnexion
          </button>
        </form>
      </div>
    </header>

    <div class="content-wrapper">
      <!-- Les pages inclues afficheront leur contenu ici -->
      <jsp:include page="${contentPage}" />
    </div>
  </main>
</div>
</body>
</html>
