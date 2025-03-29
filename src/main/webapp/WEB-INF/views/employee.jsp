<%--
  Created by IntelliJ IDEA.
  User: habib
  Date: 21/03/2025
  Time: 19:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Gestion des Employés</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<div class="container">
    <h1>Gestion des Employés</h1>

    <!-- Message display -->
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

    <!-- Search form -->
    <div class="search-container card">
        <h3><i class="fas fa-search"></i> Rechercher des Employés</h3>
        <form action="${pageContext.request.contextPath}/employees" method="get">
            <input type="hidden" name="action" value="search">

            <div class="form-row">
                <div class="form-group">
                    <label for="term"><i class="fas fa-user"></i> Nom :</label>
                    <input type="text" id="term" name="term" class="form-control" value="${searchTerm}" placeholder="Rechercher par nom...">
                </div>

                <div class="form-group">
                    <label for="departmentId"><i class="fas fa-building"></i> Département :</label>
                    <select id="departmentId" name="departmentId" class="form-control">
                        <option value="">Tous les Départements</option>
                        <c:forEach items="${departments}" var="department">
                            <option value="${department.id}" ${selectedDepartment == department.id ? 'selected' : ''}>${department.nom}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="role"><i class="fas fa-briefcase"></i> Rôle :</label>
                    <select id="role" name="role" class="form-control">
                        <option value="">Tous les Rôles</option>
                        <option value="MANAGER" ${employee.role == 'MANAGER' ? 'selected' : ''}>Manager</option>
                        <option value="EMPLOYEE" ${employee.role == 'EMPLOYEE' ? 'selected' : ''}>Employé</option>
                        <option value="ADMIN" ${employee.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
                    </select>
                </div>
            </div>

            <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Rechercher</button>
            <a href="${pageContext.request.contextPath}/employees" class="btn btn-secondary"><i class="fas fa-undo"></i> Réinitialiser</a>
        </form>
    </div>

    <!-- Employee Table -->
    <div class="table-container card">
        <div class="table-header">
            <h3><i class="fas fa-users"></i> Liste des Employés</h3>
            <a href="${pageContext.request.contextPath}/employees?action=new" class="btn btn-success">
                <i class="fas fa-plus"></i> Ajouter un Employé
            </a>
        </div>

        <table class="table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Nom</th>
                <th>Email</th>
                <th>Département</th>
                <th>Rôle</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${employees}" var="employee">
                <tr>
                    <td>${employee.id}</td>
                    <td>${employee.prenom} ${employee.nom}</td>
                    <td>${employee.email}</td>
                    <td>
                        <c:choose>
                            <c:when test="${employee.department != null}">
                                <span class="badge badge-department">${employee.department.nom}</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-secondary">Non Assigné</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <span class="badge badge-role">${employee.role}</span>
                    </td>
                    <td class="actions">
                        <a href="${pageContext.request.contextPath}/employees?action=view&id=${employee.id}"
                           class="btn btn-info btn-sm" title="Voir détails">
                            <i class="fas fa-eye"></i> Détails
                        </a>
                        <a href="${pageContext.request.contextPath}/employees?action=edit&id=${employee.id}"
                           class="btn btn-warning btn-sm" title="Modifier">
                            <i class="fas fa-edit"></i>
                        </a>
                        <button onclick="confirmDelete(${employee.id}, '${employee.prenom} ${employee.nom}')"
                                class="btn btn-danger btn-sm" title="Supprimer">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty employees}">
                <tr>
                    <td colspan="6" class="text-center">Aucun employé trouvé</td>
                </tr>
            </c:if>
            </tbody>
        </table>

        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/employees?page=${currentPage - 1}">
                        <i class="fas fa-chevron-left"></i>
                    </a>
                </c:if>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${currentPage eq i}">
                            <span class="current-page">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/employees?page=${i}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/employees?page=${currentPage + 1}">
                        <i class="fas fa-chevron-right"></i>
                    </a>
                </c:if>
            </div>
        </c:if>
    </div>
</div>

<!-- Confirmation Suppression Modal -->
<div id="deleteModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3 class="modal-title"><i class="fas fa-exclamation-triangle"></i> Confirmer la Suppression</h3>
            <button type="button" onclick="closeModal()" class="close-btn">&times;</button>
        </div>
        <div class="modal-body">
            <p>Êtes-vous sûr de vouloir supprimer l'employé : <strong><span id="employeeName"></span></strong> ?</p>
            <p class="text-danger"><i class="fas fa-exclamation-circle"></i> Cette action est irréversible.</p>
        </div>
        <div class="modal-footer">
            <button onclick="closeModal()" class="btn btn-secondary"><i class="fas fa-times"></i> Annuler</button>
            <form id="deleteForm" action="${pageContext.request.contextPath}/employees?action=delete&id=${employee.id}"  style="margin:0;">
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

        const modal = document.getElementById('deleteModal');
        const employeeNameSpan = document.getElementById('employeeName');
        const deleteIdInput = document.getElementById('deleteId');
        const deleteForm = document.getElementById('deleteForm');

        employeeNameSpan.textContent = name;
        deleteIdInput.value = id;

        deleteForm.action = `${pageContext.request.contextPath}/employees?action=delete&id=${id}`;

        modal.classList.add('show');
        document.body.style.overflow = 'hidden';

        return false;
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