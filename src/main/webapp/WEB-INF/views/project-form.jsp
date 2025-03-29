<%--
  Created by IntelliJ IDEA.
  User: habib
  Date: 24/03/2025
  Time: 16:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.app.management.companymanagement.helpers.DateUtil" %>

<html>
<head>
    <title>Formulaire Projet</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="container">
    <h1>Formulaire Projet</h1>

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

    <form action="${pageContext.request.contextPath}/projects" method="post" id="projectForm">
        <input type="hidden" name="id" value="${not empty project.id ? project.id : ''}" />

        <div class="form-group">
            <label for="nom">Nom * :</label>
            <input type="text" id="nom" name="nom" value="${not empty project.nom ? project.nom : ''}"
                   class="form-control ${not empty fieldErrors.nom ? 'is-invalid' : ''}" required />
            <c:if test="${not empty fieldErrors.nom}">
                <div class="invalid-feedback">${fieldErrors.nom}</div>
            </c:if>
        </div>

        <div class="form-group">
            <label for="description">Description :</label>
            <textarea id="description" name="description"
                      class="form-control ${not empty fieldErrors.description ? 'is-invalid' : ''}">${not empty project.description ? project.description : ''}</textarea>
            <c:if test="${not empty fieldErrors.description}">
                <div class="invalid-feedback">${fieldErrors.description}</div>
            </c:if>
        </div>

        <div class="form-group">
            <label for="date_debut">Date de début * :</label>
            <input type="date" id="date_debut" name="date_debut"
                   value="${not empty project.dateDebut ? DateUtil.formatDateForInput(project.dateDebut) : ''}"
                   class="form-control ${not empty fieldErrors.date_debut ? 'is-invalid' : ''}" required />
            <c:if test="${not empty fieldErrors.date_debut}">
                <div class="invalid-feedback">${fieldErrors.date_debut}</div>
            </c:if>
        </div>

        <div class="form-group">
            <label for="date_fin">Date de fin :</label>
            <input type="date" id="date_fin" name="date_fin"
                   value="${not empty project.dateFin ? DateUtil.formatDateForInput(project.dateFin) : ''}"
                   class="form-control ${not empty fieldErrors.date_fin ? 'is-invalid' : ''}" />
            <c:if test="${not empty fieldErrors.date_fin}">
                <div class="invalid-feedback">${fieldErrors.date_fin}</div>
            </c:if>
        </div>

        <div class="form-group">
            <label for="statut">Statut :</label>
            <select id="statut" name="statut" class="form-control">
                <option value="En cours" ${not empty project.status && project.status == 'En cours' ? 'selected' : ''}>En cours</option>
                <option value="Terminé" ${not empty project.status && project.status == 'Terminé' ? 'selected' : ''}>Terminé</option>
            </select>
        </div>

        <div class="button-group">
            <button type="submit" class="btn btn-primary">Enregistrer</button>
            <a href="${pageContext.request.contextPath}/projects" class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</div>

<script>
    document.getElementById('projectForm').addEventListener('submit', function(e) {
        let isValid = true;

        // Validation du nom
        const nom = document.getElementById('nom');
        if (!nom.value.trim()) {
            showError(nom, 'Le nom est obligatoire');
            isValid = false;
        } else if (nom.value.trim().length > 100) {
            showError(nom, 'Le nom ne doit pas dépasser 100 caractères');
            isValid = false;
        }

        // Validation de la description
        const description = document.getElementById('description');
        if (description.value.length > 500) {
            showError(description, 'La description ne doit pas dépasser 500 caractères');
            isValid = false;
        }

        // Validation des dates
        const dateDebut = document.getElementById('date_debut');
        if (!dateDebut.value) {
            showError(dateDebut, 'La date de début est obligatoire');
            isValid = false;
        }

        const dateFin = document.getElementById('date_fin');
        if (dateFin.value && new Date(dateFin.value) < new Date(dateDebut.value)) {
            showError(dateFin, 'La date de fin doit être postérieure à la date de début');
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
