<%--
  Created by IntelliJ IDEA.
  User: habib
  Date: 29/03/2025
  Time: 14:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Accès refusé</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="error-container">
    <h1><i class="fas fa-ban"></i> 403 - Accès Refusé</h1>
    <p>Vous n'avez pas les permissions nécessaires pour accéder à cette ressource.</p>
    <a href="${pageContext.request.contextPath}/dashboard" class="btn">Retour au tableau de bord</a>
</div>
</body>
</html>
