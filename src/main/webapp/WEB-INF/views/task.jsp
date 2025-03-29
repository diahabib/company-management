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
    <title>Gestion des Tâches</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .task-card { transition: all 0.3s ease; }
        .task-card:hover { transform: translateY(-2px); box-shadow: 0 6px 12px rgba(0,0,0,0.1); }
        .status-badge { padding: 0.35em 0.65em; border-radius: 4px; font-size: 0.85em; }
        .status-a-faire { background-color: #ffebee; color: #c62828; }
        .status-en-cours { background-color: #fff8e1; color: #f57f17; }
        .status-termine { background-color: #e8f5e9; color: #2e7d32; }
    </style>
</head>
<body>
<div class="container">
    <h1><i class="fas fa-tasks"></i> Gestion des Tâches</h1>

    <!-- Messages de succès/erreur -->
    <c:if test="${not empty sessionScope.successMessage}">
        <div class="alert alert-success fade-in">
            <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
            <c:remove var="successMessage" scope="session"/>
        </div>
    </c:if>
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger fade-in">
            <i class="fas fa-exclamation-circle"></i> ${sessionScope.errorMessage}
            <c:remove var="errorMessage" scope="session"/>
        </div>
    </c:if>

    <!-- Bouton Ajouter une tâche -->
    <div class="button-group">
        <a href="${pageContext.request.contextPath}/tasks?action=new" class="btn btn-success">
            <i class="fas fa-plus"></i> Ajouter une tâche
        </a>
    </div>

    <!-- Filtrer les tâches -->
    <div class="search-container card">
        <h3><i class="fas fa-filter"></i> Filtrer les tâches</h3>
        <form action="${pageContext.request.contextPath}/tasks" method="get">
            <input type="hidden" name="action" value="filter">
            <div class="form-row">
                <div class="form-group">
                    <label for="employeeId"><i class="fas fa-user"></i> Employé :</label>
                    <select id="employeeId" name="employeeId" class="form-control">
                        <option value="">Tous les employés</option>
                        <c:forEach items="${employees}" var="employee">
                            <option value="${employee.id}"
                                ${selectedEmployeeId eq employee.id ? 'selected' : ''}>
                                    ${employee.prenom} ${employee.nom}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="projectId"><i class="fas fa-project-diagram"></i> Projet :</label>
                    <select id="projectId" name="projectId" class="form-control">
                        <option value="">Tous les projets</option>
                        <c:forEach items="${projects}" var="project">
                            <option value="${project.id}"
                                ${selectedProjectId eq project.id ? 'selected' : ''}>
                                    ${project.nom}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="status"><i class="fas fa-info-circle"></i> Statut :</label>
                    <select id="status" name="status" class="form-control">
                        <option value="">Tous les statuts</option>
                        <option value="À faire" ${selectedStatus eq 'à faire' ? 'selected' : ''}>À faire</option>
                        <option value="En cours" ${selectedStatus eq 'en cours' ? 'selected' : ''}>En cours</option>
                        <option value="Terminée" ${selectedStatus eq 'terminé' ? 'selected' : ''}>Terminé</option>
                    </select>
                </div>
            </div>
            <div class="button-group">
                <button type="submit" class="btn btn-primary"><i class="fas fa-filter"></i> Filtrer</button>
                <a href="${pageContext.request.contextPath}/tasks" class="btn btn-secondary"><i class="fas fa-undo"></i> Réinitialiser</a>
            </div>
        </form>
    </div>

    <!-- Tableau des tâches -->
    <div class="table-container card task-card">
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Description</th>
                <th>Statut</th>
                <th>Assigné à</th>
                <th>Projet</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${tasks}" var="task">
                <tr>
                    <td>#${task.id}</td>
                    <td><strong>${task.description}</strong></td>
                    <td>
                        <span class="status-badge status-${task.statut.replace(' ', '-')}">
                            <c:choose>
                                <c:when test="${task.statut == 'à faire'}">
                                    <i class="far fa-circle"></i> À faire
                                </c:when>
                                <c:when test="${task.statut == 'en cours'}">
                                    <i class="fas fa-spinner fa-pulse"></i> En cours
                                </c:when>
                                <c:when test="${task.statut == 'terminé'}">
                                    <i class="fas fa-check-circle"></i> Terminé
                                </c:when>
                                <c:otherwise>
                                    ${task.statut}
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </td>
                    <td>
                        <c:if test="${not empty task.employee}">
                            <i class="fas fa-user"></i> ${task.employee.prenom} ${task.employee.nom}
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${not empty task.project}">
                            <i class="fas fa-project-diagram"></i> ${task.project.nom}
                        </c:if>
                    </td>
                    <td class="actions">
                        <a href="${pageContext.request.contextPath}/tasks?action=view&id=${task.id}"
                           class="btn btn-info btn-sm" title="Voir détails">
                            <i class="fas fa-eye"></i> Détails
                        </a>
                        <a href="${pageContext.request.contextPath}/tasks?action=edit&id=${task.id}"
                           class="btn btn-warning btn-sm" title="Modifier">
                            <i class="fas fa-edit"></i>
                        </a>
                        <button onclick="confirmDelete(${task.id}, '${task.description}')"
                                class="btn btn-danger btn-sm" title="Supprimer">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty tasks}">
                <tr>
                    <td colspan="6" class="text-center">
                        <div class="empty-state">
                            <i class="fas fa-tasks fa-3x"></i>
                            <h4>Aucune tâche trouvée</h4>
                            <a href="${pageContext.request.contextPath}/tasks?action=new" class="btn btn-primary">
                                <i class="fas fa-plus"></i> Créer une nouvelle tâche
                            </a>
                        </div>
                    </td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<!-- Modale de confirmation de suppression -->
<div id="deleteModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3 class="modal-title"><i class="fas fa-exclamation-triangle text-warning"></i> Confirmer la Suppression</h3>
            <button type="button" onclick="closeModal()" class="close-btn">&times;</button>
        </div>
        <div class="modal-body">
            <p>Êtes-vous sûr de vouloir supprimer la tâche : <strong><span id="taskDescription"></span></strong> ?</p>
            <div class="alert alert-warning">
                <i class="fas fa-exclamation-circle"></i> <strong>Attention :</strong> Cette action est irréversible.
            </div>
        </div>
        <div class="modal-footer">
            <button onclick="closeModal()" class="btn btn-secondary">
                <i class="fas fa-times"></i> Annuler
            </button>
            <form id="deleteForm" action="${pageContext.request.contextPath}/tasks?action=delete&id=${project.id}" style="margin:0;">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" id="deleteId" name="id" value="">
                <button type="submit" class="btn btn-danger">
                    <i class="fas fa-trash-alt"></i> Confirmer la suppression
                </button>
            </form>
        </div>
    </div>
</div>

<script>
    function confirmDelete(id, description) {
        event.preventDefault();
        document.getElementById('deleteId').value = id;
        document.getElementById('taskDescription').textContent = description;
        document.getElementById('deleteModal').classList.add('show');
        document.body.style.overflow = 'hidden';
    }

    function closeModal() {
        document.getElementById('deleteModal').classList.remove('show');
        document.body.style.overflow = '';
    }

    window.onclick = function(event) {
        const modal = document.getElementById('deleteModal');
        if (event.target === modal) {
            closeModal();
        }
    }

    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            closeModal();
        }
    });
</script>
</body>
</html>