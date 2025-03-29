<%--
  Created by IntelliJ IDEA.
  User: habib
  Date: 24/03/2025
  Time: 18:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Détails de la Tâche</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
    <h1>Détails de la Tâche</h1>

    <div class="card">
        <div class="card-header">
            <h2>Tâche #${task.id} : ${task.description}</h2>
        </div>
        <div class="card-body">

            <div class="detail-row">
                <div class="detail-label">Assignée à :</div>
                <div class="detail-value">${task.employee.prenom} ${task.employee.nom}</div>
            </div>
            <div class="detail-row">
                <div class="detail-label">Projet :</div>
                <div class="detail-value">${task.project.nom}</div>
            </div>
            <div class="detail-row">
                <div class="detail-label">Statut :</div>
                <div class="detail-value">
                    <span class="status-badge ${task.statut}">${task.statut}</span>
                </div>
            </div>
        </div>
    </div>

    <div class="button-group mt-4">
        <a href="${pageContext.request.contextPath}/tasks?action=edit&id=${task.id}" class="btn btn-warning">Modifier la Tâche</a>
        <a href="${pageContext.request.contextPath}/tasks" class="btn btn-secondary">Retour à la Liste</a>
    </div>
</div>
</body>
</html>