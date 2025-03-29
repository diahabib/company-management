<%--
  Created by IntelliJ IDEA.
  User: habib
  Date: 29/03/2025
  Time: 14:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Erreur 500 - Erreur Serveur</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .error-container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 2rem;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .error-icon {
            font-size: 4rem;
            color: #dc3545;
            margin-bottom: 1rem;
        }

        .error-actions {
            margin-top: 2rem;
        }

        .btn {
            display: inline-block;
            padding: 0.5rem 1rem;
            margin: 0 0.5rem;
            border-radius: 4px;
            text-decoration: none;
            color: white;
            background-color: #4361ee;
            transition: background-color 0.3s;
        }

        .btn:hover {
            background-color: #3a56d4;
        }

        .technical-details {
            margin-top: 2rem;
            padding: 1rem;
            background-color: #f8f9fa;
            border-radius: 4px;
            text-align: left;
            font-family: monospace;
            overflow-x: auto;
        }

        .hide-details {
            display: none;
        }
    </style>
</head>
<body>
<div class="error-container">
    <div class="error-icon">
        <i class="fas fa-exclamation-triangle"></i>
    </div>
    <h1>Erreur 500 - Erreur Interne du Serveur</h1>
    <p>Une erreur inattendue s'est produite. Notre équipe technique a été notifiée.</p>

    <div class="error-actions">
        <a href="${pageContext.request.contextPath}/dashboard" class="btn">
            <i class="fas fa-home"></i> Retour à l'accueil
        </a>
        <button onclick="toggleDetails()" class="btn" style="background-color: #6c757d;">
            <i class="fas fa-info-circle"></i> Détails techniques
        </button>
    </div>

    <div id="technicalDetails" class="technical-details hide-details">
        <h3>Détails de l'erreur :</h3>
        <p><strong>Message :</strong> ${not empty errorMessage ? errorMessage : 'Aucun détail disponible'}</p>

        <c:if test="${not empty exception}">
            <p><strong>Exception :</strong> ${exception.class.name}</p>
            <p><strong>Message d'exception :</strong> ${exception.message}</p>

            <h4>StackTrace :</h4>
            <pre><c:forEach items="${exception.stackTrace}" var="trace">${trace}
            </c:forEach></pre>
        </c:if>

        <c:if test="${not empty requestScope['jakarta.servlet.error.exception']}">
            <h4>Informations supplémentaires :</h4>
            <p><strong>URI :</strong> ${requestScope['jakarta.servlet.error.request_uri']}</p>
            <p><strong>Code statut :</strong> ${requestScope['jakarta.servlet.error.status_code']}</p>
            <p><strong>Servlet :</strong> ${requestScope['jakarta.servlet.error.servlet_name']}</p>
        </c:if>
    </div>
</div>

<script>
    function toggleDetails() {
        const details = document.getElementById('technicalDetails');
        details.classList.toggle('hide-details');
    }
</script>
</body>
</html>