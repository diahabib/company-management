package com.app.management.companymanagement.controller;

import com.app.management.companymanagement.model.Department;
import com.app.management.companymanagement.model.Employee;
import com.app.management.companymanagement.model.Project;
import com.app.management.companymanagement.services.ProjectService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/projects")
public class ProjectServlet extends HttpServlet {

    @EJB
    private ProjectService projectService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteProject(request, response);
                break;
            case "view":
                viewProject(request, response);
                break;
            default:
                listProjects(request, response);
                break;
        }
    }

    private void listProjects(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Project> projects = projectService.getAllProjects();
        request.setAttribute("projects", projects);
        request.getRequestDispatcher("/WEB-INF/views/project.jsp").forward(request, response);
    }

    private void viewProject(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Project project = projectService.getProject(id);

            if (project == null) {
                request.setAttribute("error", "Project not found");
                listProjects(request, response);
                return;
            }

            request.setAttribute("project", project);
            request.getRequestDispatcher("/WEB-INF/views/project-view.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid project ID");
            listProjects(request, response);
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/project-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Project existingProject = projectService.getProject(id);
        request.setAttribute("project", existingProject);
        request.getRequestDispatcher("/WEB-INF/views/project-form.jsp").forward(request, response);
    }

    private void deleteProject(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            projectService.deleteProject(id);
            request.getSession().setAttribute("successMessage", "Projet supprimé avec succès");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Erreur lors de la suppression: " + e.getMessage());
        }
        response.sendRedirect("projects");
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String nom = request.getParameter("nom");
        String description = request.getParameter("description");
        String dateDebut = request.getParameter("date_debut");
        String dateFin = request.getParameter("date_fin");
        String status = request.getParameter("statut");

        // Validation des champs obligatoires
        if (nom == null || nom.trim().isEmpty() || dateDebut == null || dateDebut.trim().isEmpty()) {
            handleError(request, response, "Le nom et la date de début sont obligatoires", null);
            return;
        }

        try {
            Date startDate = Date.valueOf(dateDebut.trim());
            Date endDate = (dateFin != null && !dateFin.trim().isEmpty()) ? Date.valueOf(dateFin.trim()) : null;

            if (endDate != null && endDate.before(startDate)) {
                handleError(request, response, "La date de fin doit être postérieure à la date de début",
                        new Project(nom, description, startDate, endDate, status));
                return;
            }

            Project project = new Project(nom.trim(),
                    description != null ? description.trim() : null,
                    startDate,
                    endDate,
                    status != null ? status : "En cours");

            if (idParam == null || idParam.isEmpty()) {
                createProject(request, response, project);
            } else {
                updateProject(request, response, project, idParam);
            }
        } catch (IllegalArgumentException e) {
            handleError(request, response, "Format de date invalide",
                    new Project(nom, description, null, null, status));
        } catch (Exception e) {
            handleError(request, response, "Une erreur technique est survenue. Veuillez réessayer.", null);
        }
    }

    private void createProject(HttpServletRequest request, HttpServletResponse response,
                               Project project)
            throws ServletException, IOException {
        try {
            if (projectService.projectExists(project.getNom())) {
                handleError(request, response, "Un projet avec ce nom existe déjà", project);
                return;
            }

            projectService.addProject(project);
            request.getSession().setAttribute("successMessage", "Projet créé avec succès");
            response.sendRedirect("projects");
        } catch (Exception e) {
            handleDatabaseError(request, response, e, project);
        }
    }

    private void updateProject(HttpServletRequest request, HttpServletResponse response,
                               Project project, String idParam)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(idParam);
            project.setId(id);

            Project existing = projectService.getProject(id);
            if (existing == null) {
                handleError(request, response, "Le projet à modifier n'existe pas", project);
                return;
            }

            if (!existing.getNom().equals(project.getNom()) &&
                    projectService.projectExists(project.getNom())) {
                handleError(request, response, "Un autre projet avec ce nom existe déjà", project);
                return;
            }

            projectService.updateProject(project);
            request.getSession().setAttribute("successMessage", "Projet mis à jour avec succès");
            response.sendRedirect("projects");
        } catch (NumberFormatException e) {
            handleError(request, response, "ID de projet invalide", project);
        } catch (Exception e) {
            handleDatabaseError(request, response, e, project);
        }
    }

    private void handleDatabaseError(HttpServletRequest request, HttpServletResponse response,
                                     Exception e, Project project)
            throws ServletException, IOException {
        String rootMsg = getRootCauseMessage(e);

        if (rootMsg.contains("constraint")) {
            if (rootMsg.contains("nom")) {
                handleError(request, response, "Ce nom de projet est déjà utilisé", project);
            } else if (rootMsg.contains("foreign key")) {
                handleError(request, response, "Impossible de supprimer: projet lié à des tâches ou employés", project);
            } else {
                handleError(request, response, "Violation de contrainte: " + rootMsg, project);
            }
        } else {
            handleError(request, response, "Erreur de base de données: " + rootMsg, project);
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String message, Project project)
            throws ServletException, IOException {
        if (project == null) {
            project = new Project();
        }

        request.setAttribute("error", message);
        request.setAttribute("project", project);

        if (project.getId() == null) {
            request.getRequestDispatcher("/WEB-INF/views/project-form.jsp").forward(request, response);
        } else {
            showEditForm(request, response);
        }
    }

    private String getRootCauseMessage(Throwable e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }


}
