<%--
  Created by IntelliJ IDEA.
  User: habib
  Date: 22/03/2025
  Time: 20:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Gestion des Départements</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<div class="container">
    <h1><i class="fas fa-building"></i> Gestion des Départements</h1>

    <!-- Messages de succès et d'erreur -->
    <c:if test="${not empty sessionScope.successMessage}">
        <div class="alert alert-success">
            <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
            <c:remove var="successMessage" scope="session"/>
        </div>
    </c:if>
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger">
            <i class="fas fa-exclamation-circle"></i> ${sessionScope.errorMessage}
            <c:remove var="errorMessage" scope="session"/>
        </div>
    </c:if>

    <!-- Bouton Ajouter un Département -->
    <div class="button-group">
        <a href="${pageContext.request.contextPath}/departments?action=new" class="btn btn-success">
            <i class="fas fa-plus"></i> Ajouter un Département
        </a>
    </div>

    <!-- Tableau des Départements -->
    <div class="table-container card">
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Nom</th>
                <th>Description</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${departments}" var="department">
                <tr>
                    <td>${department.id}</td>
                    <td><strong>${department.nom}</strong></td>
                    <td>${department.description}</td>
                    <td class="actions">
                        <a href="${pageContext.request.contextPath}/departments?action=view&id=${department.id}"
                           class="btn btn-info btn-sm" title="Voir détails">
                            <i class="fas fa-eye"></i> Détails
                        </a>
                        <a href="${pageContext.request.contextPath}/departments?action=edit&id=${department.id}"
                           class="btn btn-warning btn-sm" title="Modifier">
                            <i class="fas fa-edit"></i>
                        </a>
                        <button onclick="confirmDelete(${department.id}, '${department.nom}')"
                                class="btn btn-danger btn-sm" title="Supprimer">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty departments}">
                <tr>
                    <td colspan="4" class="text-center">Aucun département trouvé</td>
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
            <h3 class="modal-title"><i class="fas fa-exclamation-triangle"></i> Confirmer la Suppression</h3>
            <button type="button" onclick="closeModal()" class="close-btn">&times;</button>
        </div>
        <div class="modal-body">
            <p>Êtes-vous sûr de vouloir supprimer le département : <strong><span id="departmentName"></span></strong> ?</p>
            <p class="text-danger"><i class="fas fa-exclamation-circle"></i> Cette action est irréversible.</p>
        </div>
        <div class="modal-footer">
            <button onclick="closeModal()" class="btn btn-secondary"><i class="fas fa-times"></i> Annuler</button>
            <form id="deleteForm" action="${pageContext.request.contextPath}/departments?action=delete&id=${department.id}"  style="margin:0;">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" id="deleteId" name="id" value="">
                <button type="submit" class="btn btn-danger"><i class="fas fa-trash"></i> Supprimer</button>
            </form>
        </div>
    </div>
</div>

<script>
    function confirmDelete(id, name) {
        event.preventDefault();
        document.getElementById('deleteId').value = id;
        document.getElementById('departmentName').textContent = name;
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
