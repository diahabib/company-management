package com.app.management.companymanagement.controller;

import com.app.management.companymanagement.exceptions.ServiceException;
import com.app.management.companymanagement.model.Department;
import com.app.management.companymanagement.model.Employee;
import com.app.management.companymanagement.model.Project;
import com.app.management.companymanagement.model.Task;
import com.app.management.companymanagement.services.EmployeeService;
import com.app.management.companymanagement.services.ProjectService;
import com.app.management.companymanagement.services.TaskService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {

    @EJB
    private TaskService taskService;

    @EJB
    private EmployeeService employeeService;

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
                deleteTask(request, response);
                break;
            case "view":
                viewTask(request, response);
                break;
            case "employee":
                listTasksByEmployee(request, response);
                break;
            case "project":
                listTasksByProject(request, response);
                break;
            case "filter":
                filterTasks(request, response);
                break;
            default:
                listTasks(request, response);
                break;
        }

    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Employee> employees = employeeService.getAllEmployees();
        List<Project> projects = projectService.getAllProjects();

        request.setAttribute("employees", employees);
        request.setAttribute("projects", projects);
        request.getRequestDispatcher("/WEB-INF/views/task-form.jsp").forward(request, response);
    }

    private void viewTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Task task = taskService.getTask(id);

        if (task == null) {
            request.setAttribute("error", "Task not found");
            listTasks(request, response);
            return;
        }

        request.setAttribute("task", task);
        request.getRequestDispatcher("/WEB-INF/views/task-view.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Task existingTask = taskService.getTask(id);

        List<Employee> employees = employeeService.getAllEmployees();
        List<Project> projects = projectService.getAllProjects();

        request.setAttribute("task", existingTask);
        request.setAttribute("employees", employees);
        request.setAttribute("projects", projects);
        request.getRequestDispatcher("/WEB-INF/views/task-form.jsp").forward(request, response);
    }

    private void deleteTask(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            taskService.deleteTask(id);
            request.getSession().setAttribute("successMessage", "Tâche supprimé avec succès");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Erreur lors de la suppression: " + e.getMessage());
        }
        response.sendRedirect("tasks");
    }

    private void listTasks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Project> projects = projectService.getAllProjects();
        List<Employee> employees = employeeService.getAllEmployees();
        List<Task> tasks = taskService.getAllTasks();

        request.setAttribute("employees", employees);
        request.setAttribute("projects", projects);
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("/WEB-INF/views/task.jsp").forward(request, response);
    }

    private void listTasksByEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long employeeId = Long.parseLong(request.getParameter("id"));
        List<Task> tasks = taskService.getTasksByEmployee(employeeId);
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("/WEB-INF/views/task.jsp").forward(request, response);
    }

    private void listTasksByProject(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long projectId = Long.parseLong(request.getParameter("id"));
        List<Task> tasks = taskService.getTasksByProject(projectId);
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("/WEB-INF/views/task.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        String employeeIdStr = request.getParameter("employeeId");
        String projectIdStr = request.getParameter("projectId");

        // Préparer les données pour la vue en cas d'erreur
        List<Employee> employees = employeeService.getAllEmployees();
        List<Project> projects = projectService.getAllProjects();
        request.setAttribute("employees", employees);
        request.setAttribute("projects", projects);

        // Validation des champs obligatoires
        if (description == null || description.trim().isEmpty() ||
                status == null || status.trim().isEmpty() ||
                employeeIdStr == null || employeeIdStr.trim().isEmpty() ||
                projectIdStr == null || projectIdStr.trim().isEmpty()) {
            handleError(request, response, "Tous les champs obligatoires doivent être remplis", null);
            return;
        }

        try {
            Long employeeId = Long.parseLong(employeeIdStr);
            Long projectId = Long.parseLong(projectIdStr);

            // Vérifier que l'employé et le projet existent
            Employee employee = employeeService.getEmployee(employeeId);
            Project project = projectService.getProject(projectId);

            if (employee == null || project == null) {
                handleError(request, response, "Employé ou projet sélectionné invalide", null);
                return;
            }

            Task task = new Task();
            task.setDescription(description.trim());
            task.setStatut(status);
            task.setEmployee(employee);
            task.setProject(project);

            if (idParam == null || idParam.isEmpty()) {
                createTask(request, response, task);
            } else {
                updateTask(request, response, task, idParam);
            }
        } catch (NumberFormatException e) {
            handleError(request, response, "ID d'employé ou de projet invalide", null);
        } catch (Exception e) {
            handleError(request, response, "Une erreur technique est survenue. Veuillez réessayer.", null);
        }
    }

    private void createTask(HttpServletRequest request, HttpServletResponse response,
                            Task task)
            throws ServletException, IOException {
        try {
            taskService.addTask(task);
            request.getSession().setAttribute("successMessage", "Tâche créée avec succès");
            response.sendRedirect("tasks");
        } catch (Exception e) {
            handleDatabaseError(request, response, e, task);
        }
    }

    private void updateTask(HttpServletRequest request, HttpServletResponse response,
                            Task task, String idParam)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(idParam);
            task.setId(id);

            // Vérifier si la tâche existe
            Task existing = taskService.getTask(id);
            if (existing == null) {
                handleError(request, response, "La tâche à modifier n'existe pas", task);
                return;
            }

            taskService.updateTask(task);
            request.getSession().setAttribute("successMessage", "Tâche mise à jour avec succès");
            response.sendRedirect("tasks");
        } catch (NumberFormatException e) {
            handleError(request, response, "ID de tâche invalide", task);
        } catch (Exception e) {
            handleDatabaseError(request, response, e, task);
        }
    }

    private void handleDatabaseError(HttpServletRequest request, HttpServletResponse response,
                                     Exception e, Task task)
            throws ServletException, IOException {
        String rootMsg = getRootCauseMessage(e);

        if (rootMsg.contains("constraint")) {
            if (rootMsg.contains("foreign key")) {
                handleError(request, response, "Employé ou projet invalide", task);
            } else {
                handleError(request, response, "Violation de contrainte: " + rootMsg, task);
            }
        } else {
            handleError(request, response, "Erreur de base de données: " + rootMsg, task);
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String message, Task task)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        if (task != null) {
            request.setAttribute("task", task);
        }

        List<Employee> employees = employeeService.getAllEmployees();
        List<Project> projects = projectService.getAllProjects();
        request.setAttribute("employees", employees);
        request.setAttribute("projects", projects);

        if (task == null || task.getId() == null) {
            request.getRequestDispatcher("/WEB-INF/views/task-form.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/views/task-form.jsp").forward(request, response);
        }
    }

    private String getRootCauseMessage(Throwable e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }


    private void filterTasks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String employeeId = request.getParameter("employeeId");
        String projectId = request.getParameter("projectId");
        String status = request.getParameter("status");


        List<Task> tasks = taskService.filterTasks(employeeId, projectId, status);
        List<Employee> employees = employeeService.getAllEmployees();
        List<Project> projects = projectService.getAllProjects();

        request.setAttribute("tasks", tasks);
        request.setAttribute("employees", employees);
        request.setAttribute("projects", projects);

        request.setAttribute("selectedEmployeeId", employeeId);
        request.setAttribute("selectedProjectId", projectId);
        request.setAttribute("selectedStatus", status);

        request.getRequestDispatcher("/WEB-INF/views/task.jsp").forward(request, response);
    }
}
