<%--
  Created by IntelliJ IDEA.
  User: habib
  Date: 22/03/2025
  Time: 16:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Détails de l'Employé</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
  <h1>Détails de l'Employé</h1>

  <div class="card">
    <div class="card-header">
      <h2>${employee.nom} ${employee.prenom}</h2>
    </div>
    <div class="card-body">
      <div class="detail-row">
        <div class="detail-label">ID :</div>
        <div class="detail-value">${employee.id}</div>
      </div>
      <div class="detail-row">
        <div class="detail-label">Prénom :</div>
        <div class="detail-value">${employee.prenom}</div>
      </div>
      <div class="detail-row">
        <div class="detail-label">Nom :</div>
        <div class="detail-value">${employee.nom}</div>
      </div>
      <div class="detail-row">
        <div class="detail-label">Email :</div>
        <div class="detail-value">${employee.email}</div>
      </div>
      <div class="detail-row">
        <div class="detail-label">Département :</div>
        <div class="detail-value">${employee.department != null ? employee.department.nom : 'Non Assigné'}</div>
      </div>
      <div class="detail-row">
        <div class="detail-label">Rôle :</div>
        <div class="detail-value">${employee.role}</div>
      </div>
    </div>
  </div>

  <div class="button-group mt-4">
    <a href="${pageContext.request.contextPath}/employees?action=edit&id=${employee.id}" class="btn btn-warning">Modifier</a>
    <a href="${pageContext.request.contextPath}/employees" class="btn btn-secondary">Retour à la liste</a>
  </div>
</div>
</body>
</html>