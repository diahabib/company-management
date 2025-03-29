<%--
  Created by IntelliJ IDEA.
  User: habib
  Date: 24/03/2025
  Time: 18:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Formulaire de Tâche</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
  <h1>Formulaire de Tâche</h1>

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

  <form action="${pageContext.request.contextPath}/tasks" method="post" id="taskForm">
    <input type="hidden" name="id" value="${not empty task.id ? task.id : ''}">

    <div class="form-group">
      <label for="description">Description * :</label>
      <textarea id="description" name="description"
                class="form-control ${not empty fieldErrors.description ? 'is-invalid' : ''}"
                required>${not empty task.description ? task.description : ''}</textarea>
      <c:if test="${not empty fieldErrors.description}">
        <div class="invalid-feedback">${fieldErrors.description}</div>
      </c:if>
    </div>

    <div class="form-group">
      <label for="status">Statut * :</label>
      <select id="status" name="status"
              class="form-control ${not empty fieldErrors.status ? 'is-invalid' : ''}" required>
        <option value="">Sélectionner un statut</option>
        <option value="À faire" ${not empty task.statut && task.statut == 'À faire' ? 'selected' : ''}>À faire</option>
        <option value="En cours" ${not empty task.statut && task.statut == 'En cours' ? 'selected' : ''}>En cours</option>
        <option value="Terminée" ${not empty task.statut && task.statut == 'Terminée' ? 'selected' : ''}>Terminée</option>
      </select>
      <c:if test="${not empty fieldErrors.status}">
        <div class="invalid-feedback">${fieldErrors.status}</div>
      </c:if>
    </div>

    <div class="form-group">
      <label for="employeeId">Attribuer à * :</label>
      <select id="employeeId" name="employeeId"
              class="form-control ${not empty fieldErrors.employeeId ? 'is-invalid' : ''}" required>
        <option value="">Sélectionner un employé</option>
        <c:forEach items="${employees}" var="employee">
          <option value="${employee.id}"
            ${not empty task.employee && task.employee.id == employee.id ? 'selected' : ''}>
              ${employee.prenom} ${employee.nom}
          </option>
        </c:forEach>
      </select>
      <c:if test="${not empty fieldErrors.employeeId}">
        <div class="invalid-feedback">${fieldErrors.employeeId}</div>
      </c:if>
    </div>

    <div class="form-group">
      <label for="projectId">Projet * :</label>
      <select id="projectId" name="projectId"
              class="form-control ${not empty fieldErrors.projectId ? 'is-invalid' : ''}" required>
        <option value="">Sélectionner un projet</option>
        <c:forEach items="${projects}" var="project">
          <option value="${project.id}"
            ${not empty task.project && task.project.id == project.id ? 'selected' : ''}>
              ${project.nom}
          </option>
        </c:forEach>
      </select>
      <c:if test="${not empty fieldErrors.projectId}">
        <div class="invalid-feedback">${fieldErrors.projectId}</div>
      </c:if>
    </div>

    <div class="button-group">
      <button type="submit" class="btn btn-primary">Enregistrer</button>
      <a href="${pageContext.request.contextPath}/tasks" class="btn btn-secondary">Annuler</a>
    </div>
  </form>
</div>

<script>
  document.getElementById('taskForm').addEventListener('submit', function(e) {
    let isValid = true;

    // Validation de la description
    const description = document.getElementById('description');
    if (!description.value.trim()) {
      showError(description, 'La description est obligatoire');
      isValid = false;
    } else if (description.value.trim().length > 500) {
      showError(description, 'La description ne doit pas dépasser 500 caractères');
      isValid = false;
    }

    // Validation du statut
    const status = document.getElementById('status');
    if (!status.value) {
      showError(status, 'Le statut est obligatoire');
      isValid = false;
    }

    // Validation de l'employé
    const employeeId = document.getElementById('employeeId');
    if (!employeeId.value) {
      showError(employeeId, 'L\'employé est obligatoire');
      isValid = false;
    }

    // Validation du projet
    const projectId = document.getElementById('projectId');
    if (!projectId.value) {
      showError(projectId, 'Le projet est obligatoire');
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