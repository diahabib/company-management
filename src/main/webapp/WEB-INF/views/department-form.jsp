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
    <title>Formulaire Département</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
    <h1>Formulaire Département</h1>

    <%-- Affichage des messages --%>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">
                ${error}
        </div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">
                ${successMessage}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/departments" method="post" id="departmentForm">
        <input type="hidden" name="id" value="${not empty department.id ? department.id : ''}" />

        <div class="form-group">
            <label for="name">Nom du Département * :</label>
            <input type="text" id="name" name="name" value="${not empty department.nom ? department.nom : ''}"
                   class="form-control ${not empty fieldErrors.name ? 'is-invalid' : ''}" required />
            <c:if test="${not empty fieldErrors.name}">
                <div class="invalid-feedback">${fieldErrors.name}</div>
            </c:if>
        </div>

        <div class="form-group">
            <label for="description">Description :</label>
            <textarea id="description" name="description"
                      class="form-control ${not empty fieldErrors.description ? 'is-invalid' : ''}">${not empty department.description ? department.description : ''}</textarea>
            <c:if test="${not empty fieldErrors.description}">
                <div class="invalid-feedback">${fieldErrors.description}</div>
            </c:if>
        </div>

        <div class="button-group">
            <button type="submit" class="btn btn-primary">Enregistrer</button>
            <a href="${pageContext.request.contextPath}/departments" class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</div>

<script>
    document.getElementById('departmentForm').addEventListener('submit', function(e) {
        let isValid = true;
        const name = document.getElementById('name');

        if (!name.value.trim()) {
            showError(name, 'Le nom est obligatoire');
            isValid = false;
        } else if (name.value.trim().length > 50) {
            showError(name, 'Le nom ne doit pas dépasser 50 caractères');
            isValid = false;
        }

        const description = document.getElementById('description');
        if (description.value.length > 255) {
            showError(description, 'La description ne doit pas dépasser 255 caractères');
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault();
        }
    });

    function showError(element, message) {
        element.classList.add('is-invalid');
        let feedback = element.nextElementSibling;
        if (!feedback || !feedback.classList.contains('invalid-feedback')) {
            feedback = document.createElement('div');
            feedback.className = 'invalid-feedback';
            element.parentNode.appendChild(feedback);
        }
        feedback.textContent = message;
    }
</script>
</body>
</html>
