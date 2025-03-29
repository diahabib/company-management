<%--
  Created by IntelliJ IDEA.
  User: habib
  Date: 24/03/2025
  Time: 16:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Gestion des Projets</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        /* Additional custom styles */
        .project-card {
            transition: all 0.3s ease;
        }
        .project-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 12px rgba(0,0,0,0.1);
        }
        .badge-pill {
            border-radius: 10rem;
            padding: 0.35em 0.65em;
            font-size: 0.75em;
            font-weight: 700;
        }
    </style>
</head>
<body>
<div class="container">
    <h1><i class="fas fa-project-diagram"></i> Gestion des Projets</h1>

    <!-- Success and Error Messages -->
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

    <!-- Button Group -->
    <div class="button-group">
        <a href="${pageContext.request.contextPath}/projects?action=new" class="btn btn-success">
            <i class="fas fa-plus"></i> Créer un Nouveau Projet
        </a>
    </div>

    <!-- Projects Table -->
    <div class="table-container card project-card">
        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Nom</th>
                <th>Description</th>
                <th>Statut</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${projects}" var="project">
                <tr>
                    <td>#${project.id}</td>
                    <td><strong>${project.nom}</strong></td>
                    <td>${project.description}</td>
                    <td>
                        <span class="badge-pill status-badge ${project.status}">
                            <c:choose>
                                <c:when test="${project.status == 'en_cours'}">
                                    <i class="fas fa-spinner"></i> En cours
                                </c:when>
                                <c:when test="${project.status == 'termine'}">
                                    <i class="fas fa-check-circle"></i> Terminé
                                </c:when>
                                <c:when test="${project.status == 'en_attente'}">
                                    <i class="fas fa-pause-circle"></i> En attente
                                </c:when>
                                <c:otherwise>
                                    ${project.status}
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </td>
                    <td class="actions">
                        <a href="${pageContext.request.contextPath}/projects?action=view&id=${project.id}"
                           class="btn btn-info btn-sm" title="Voir détails">
                            <i class="fas fa-eye"></i> Détails
                        </a>
                        <a href="${pageContext.request.contextPath}/projects?action=edit&id=${project.id}"
                           class="btn btn-warning btn-sm" title="Modifier">
                            <i class="fas fa-edit"></i> Modifier
                        </a>
                        <button onclick="confirmDelete(${project.id}, '${project.nom}')"
                                class="btn btn-danger btn-sm" title="Supprimer">
                            <i class="fas fa-trash-alt"></i> Supprimer
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty projects}">
                <tr>
                    <td colspan="5" class="text-center">
                        <div class="empty-state">
                            <i class="fas fa-folder-open fa-3x"></i>
                            <h4>Aucun projet trouvé</h4>
                            <a href="${pageContext.request.contextPath}/projects?action=new" class="btn btn-primary">
                                <i class="fas fa-plus"></i> Créer votre premier projet
                            </a>
                        </div>
                    </td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<!-- Modern Delete Confirmation Modal -->
<div id="deleteModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3 class="modal-title"><i class="fas fa-exclamation-triangle text-warning"></i> Confirmer la Suppression</h3>
            <button type="button" onclick="closeModal()" class="close-btn">&times;</button>
        </div>
        <div class="modal-body">
            <p>Êtes-vous sûr de vouloir supprimer le projet : <strong><span id="projectName"></span></strong> ?</p>
            <div class="alert alert-warning">
                <i class="fas fa-exclamation-circle"></i> <strong>Attention :</strong> Cette action supprimera également toutes les tâches associées à ce projet.
            </div>
        </div>
        <div class="modal-footer">
            <button onclick="closeModal()" class="btn btn-secondary">
                <i class="fas fa-times"></i> Annuler
            </button>
            <form id="deleteForm" action="${pageContext.request.contextPath}/projects?action=delete&id=${project.id}"  style="margin:0;">
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
    function confirmDelete(id, name) {
        event.preventDefault();
        document.getElementById('deleteId').value = id;
        document.getElementById('projectName').textContent = name;
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