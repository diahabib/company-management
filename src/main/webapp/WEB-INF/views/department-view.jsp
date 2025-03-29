<%--
  Created by IntelliJ IDEA.
  User: habib
  Date: 22/03/2025
  Time: 20:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Détails du Département</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
    <h1>Détails du Département</h1>

    <div class="card">
        <div class="card-header">
            <h2>${department.nom}</h2>
        </div>
        <div class="card-body">
            <div class="detail-row">
                <div class="detail-label">ID :</div>
                <div class="detail-value">${department.id}</div>
            </div>
            <div class="detail-row">
                <div class="detail-label">Nom :</div>
                <div class="detail-value">${department.nom}</div>
            </div>
            <div class="detail-row">
                <div class="detail-label">Description :</div>
                <div class="detail-value">${department.description}</div>
            </div>
        </div>
    </div>

    <div class="button-group mt-4">
        <a href="${pageContext.request.contextPath}/departments?action=edit&id=${department.id}" class="btn btn-warning">Modifier le Département</a>
        <a href="${pageContext.request.contextPath}/departments" class="btn btn-secondary">Retour à la liste</a>
    </div>
</div>
</body>
</html>
