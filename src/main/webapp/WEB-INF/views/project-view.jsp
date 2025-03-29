<%--
  Created by IntelliJ IDEA.
  User: habib
  Date: 24/03/2025
  Time: 16:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Détails du Projet</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
    <h1>Détails du Projet</h1>

    <div class="card">
        <div class="card-header">
            <h2>${project.nom}</h2>
        </div>
        <div class="card-body">
            <div class="detail-row">
                <div class="detail-label"><strong>ID:</strong></div>
                <div class="detail-value">${project.id}</div>
            </div>
            <div class="detail-row">
                <div class="detail-label"><strong>Description:</strong></div>
                <div class="detail-value">${project.description}</div>
            </div>
            <div class="detail-row">
                <div class="detail-label"><strong>Date de début:</strong></div>
                <div class="detail-value">${project.dateDebut}</div>
            </div>
            <div class="detail-row">
                <div class="detail-label"><strong>Date de fin:</strong></div>
                <div class="detail-value">${project.dateFin}</div>
            </div>
            <div class="detail-row">
                <div class="detail-label"><strong>Statut:</strong></div>
                <div class="detail-value">
                    <span class="status-badge ${project.status}">${project.status}</span>
                </div>
            </div>
        </div>
    </div>

    <div class="button-group mt-4">
        <a href="${pageContext.request.contextPath}/projects?action=edit&id=${project.id}" class="btn btn-warning">Modifier</a>
        <a href="${pageContext.request.contextPath}/projects" class="btn btn-secondary">Retour</a>
    </div>
</div>
</body>
</html>
