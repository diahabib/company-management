<%--
  Created by IntelliJ IDEA.
  User: habib
  Date: 21/03/2025
  Time: 21:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des Employés</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
    <h1>Formulaire d'Employé</h1>

    <%-- Affichage des erreurs --%>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">
            <strong>Erreur :</strong> ${error}
            <c:if test="${not empty errorDetails}">
                <div class="error-details">${errorDetails}</div>
            </c:if>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/employees" method="post" id="employeeForm">
        <input type="hidden" name="id" value="${not empty employee and employee.id != null ? employee.id : ''}" />

        <div class="form-group">
            <label for="nom">Nom * :</label>
            <input type="text" id="nom" name="nom" value="${employee.nom}"
                   class="form-control ${not empty fieldErrors.nom ? 'is-invalid' : ''}" required />
            <c:if test="${not empty fieldErrors.nom}">
                <div class="invalid-feedback">${fieldErrors.nom}</div>
            </c:if>
        </div>

        <div class="form-group">
            <label for="prenom">Prénom * :</label>
            <input type="text" id="prenom" name="prenom" value="${employee.prenom}"
                   class="form-control ${not empty fieldErrors.prenom ? 'is-invalid' : ''}" required />
            <c:if test="${not empty fieldErrors.prenom}">
                <div class="invalid-feedback">${fieldErrors.prenom}</div>
            </c:if>
        </div>

        <div class="form-group">
            <label for="email">Email * :</label>
            <input type="email" id="email" name="email" value="${employee.email}"
                   class="form-control ${not empty fieldErrors.email ? 'is-invalid' : ''}" required />
            <c:if test="${not empty fieldErrors.email}">
                <div class="invalid-feedback">${fieldErrors.email}</div>
            </c:if>
        </div>

        <div class="form-group">
            <label for="role">Rôle * :</label>
            <select id="role" name="role" class="form-control ${not empty fieldErrors.role ? 'is-invalid' : ''}" required>
                <option value="">Sélectionner un Rôle</option>
                <option value="MANAGER" ${employee.role == 'MANAGER' ? 'selected' : ''}>Manager</option>
                <option value="EMPLOYEE" ${employee.role == 'EMPLOYEE' ? 'selected' : ''}>Employé</option>
                <option value="ADMIN" ${employee.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
            </select>
            <c:if test="${not empty fieldErrors.role}">
                <div class="invalid-feedback">${fieldErrors.role}</div>
            </c:if>
        </div>

        <div class="form-group">
            <label for="departmentId">Département * :</label>
            <select id="departmentId" name="departmentId"
                    class="form-control ${not empty fieldErrors.departmentId ? 'is-invalid' : ''}" required>
                <option value="">Sélectionner un Département</option>
                <c:forEach items="${departments}" var="department">
                    <option value="${department.id}"
                        ${employee.department != null && employee.department.id == department.id ? 'selected' : ''}>
                            ${department.nom}
                    </option>
                </c:forEach>
            </select>
            <c:if test="${not empty fieldErrors.departmentId}">
                <div class="invalid-feedback">${fieldErrors.departmentId}</div>
            </c:if>
        </div>

        <div class="button-group">
            <button type="submit" class="btn btn-primary">Confirmer</button>
            <a href="${pageContext.request.contextPath}/employees" class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.getElementById('employeeForm');

        form.addEventListener('submit', function(event) {
            let isValid = true;

            // Validation du nom
            const nom = document.getElementById('nom');
            if (!nom.value.trim()) {
                showError(nom, 'Le nom est obligatoire');
                isValid = false;
            }

            // Validation du prénom
            const prenom = document.getElementById('prenom');
            if (!prenom.value.trim()) {
                showError(prenom, 'Le prénom est obligatoire');
                isValid = false;
            }

            // Validation de l'email
            const email = document.getElementById('email');
            if (!email.value.trim()) {
                showError(email, 'L\'email est obligatoire');
                isValid = false;
            } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
                showError(email, 'Format d\'email invalide');
                isValid = false;
            }

            // Validation du rôle
            const role = document.getElementById('role');
            if (!role.value) {
                showError(role, 'Le rôle est obligatoire');
                isValid = false;
            }

            // Validation du département
            const departmentId = document.getElementById('departmentId');
            if (!departmentId.value) {
                showError(departmentId, 'Le département est obligatoire');
                isValid = false;
            }

            if (!isValid) {
                event.preventDefault();
            }
        });

        function showError(element, message) {
            const feedback = element.nextElementSibling;
            if (feedback && feedback.classList.contains('invalid-feedback')) {
                feedback.textContent = message;
            } else {
                const div = document.createElement('div');
                div.className = 'invalid-feedback';
                div.textContent = message;
                element.parentNode.appendChild(div);
            }
            element.classList.add('is-invalid');
        }
    });
</script>
</body>
</html>