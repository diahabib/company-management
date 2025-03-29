<%--
  Created by IntelliJ IDEA.
  User: habib
  Date: 29/03/2025
  Time: 13:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="card">
    <h2 class="card-title">Bienvenue dans votre espace de gestion</h2>
    <p>Utilisez le menu de gauche pour naviguer entre les différentes sections.</p>

    <c:if test="${userService.hasRole(user, 'ADMIN')}">
        <div style="margin-top: 1.5rem;">
            <h3>Statistiques rapides</h3>
            <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 1rem;">
                <div class="card" style="padding: 1rem;">
                    <h4 style="margin-top: 0;">Employés</h4>
                    <p style="font-size: 2rem; margin: 0.5rem 0; color: var(--primary-color);">${employeeCount}</p>
                </div>
                <div class="card" style="padding: 1rem;">
                    <h4 style="margin-top: 0;">Départements</h4>
                    <p style="font-size: 2rem; margin: 0.5rem 0; color: var(--primary-color);">${departmentCount}</p>
                </div>
                <div class="card" style="padding: 1rem;">
                    <h4 style="margin-top: 0;">Projets</h4>
                    <p style="font-size: 2rem; margin: 0.5rem 0; color: var(--primary-color);">${projectCount}</p>
                </div>
            </div>
        </div>
    </c:if>
</div>
