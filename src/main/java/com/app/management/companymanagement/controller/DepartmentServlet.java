package com.app.management.companymanagement.controller;

import com.app.management.companymanagement.model.Department;
import com.app.management.companymanagement.model.Employee;
import com.app.management.companymanagement.services.DepartmentService;
import com.app.management.companymanagement.services.EmployeeService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/departments")
public class DepartmentServlet extends HttpServlet {

    @EJB
    private DepartmentService departmentService;

    @EJB
    private EmployeeService employeeService;

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
                deleteDepartment(request, response);
                break;
            case "view":
                viewDepartment(request, response);
                break;
            default:
                listDepartments(request, response);
                break;
        }
    }

    private void listDepartments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Department> departments = departmentService.getAllDepartments();
        request.setAttribute("departments", departments);
        request.getRequestDispatcher("/WEB-INF/views/department.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Department department = new Department();
      //  department.setId(0L);
        //request.setAttribute("department", department);
        request.getRequestDispatcher("/WEB-INF/views/department-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Department existingDepartment = departmentService.getDepartment(id);
        request.setAttribute("department", existingDepartment);
        request.getRequestDispatcher("/WEB-INF/views/department-form.jsp").forward(request, response);
    }

    private void viewDepartment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Department department = departmentService.getDepartment(id);

        if (department == null) {
            request.setAttribute("error", "Department not found");
            listDepartments(request, response);
            return;
        }

        request.setAttribute("department", department);
        request.getRequestDispatcher("/WEB-INF/views/department-view.jsp").forward(request, response);
    }

    private void deleteDepartment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            departmentService.deleteDepartment(id);
            request.getSession().setAttribute("successMessage", "Département supprimé avec succès");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Erreur lors de la suppression: " + e.getMessage());
        }
        response.sendRedirect("departments");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        // Validation des champs obligatoires
        if (name == null || name.trim().isEmpty()) {
            handleError(request, response, "Le nom du département est obligatoire", null);
            return;
        }

        Department department = new Department();
        department.setNom(name.trim());
        department.setDescription(description != null ? description.trim() : null);

        try {
            if (idParam == null || idParam.isEmpty()) {
                createDepartment(request, response, department);
            } else {
                updateDepartment(request, response, department, idParam);
            }
        } catch (Exception e) {
            handleError(request, response, "Une erreur technique est survenue. Veuillez réessayer.", department);
        }
    }

    private void createDepartment(HttpServletRequest request, HttpServletResponse response,
                                  Department department)
            throws ServletException, IOException {
        try {
            if (departmentService.departmentExists(department.getNom())) {
                handleError(request, response, "Un département avec ce nom existe déjà", department);
                return;
            }

            departmentService.addDepartment(department);
            request.getSession().setAttribute("successMessage", "Département créé avec succès");
            response.sendRedirect("departments");
        } catch (Exception e) {
            handleDatabaseError(request, response, e, department);
        }
    }

    private void updateDepartment(HttpServletRequest request, HttpServletResponse response,
                                  Department department, String idParam)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(idParam);
            department.setId(id);

            Department existing = departmentService.getDepartment(id);
            if (existing == null) {
                handleError(request, response, "Le département à modifier n'existe pas", department);
                return;
            }

            if (!existing.getNom().equals(department.getNom()) &&
                    departmentService.departmentExists(department.getNom())) {
                handleError(request, response, "Un autre département avec ce nom existe déjà", department);
                return;
            }

            departmentService.updateDepartment(department);
            request.getSession().setAttribute("successMessage", "Département mis à jour avec succès");
            response.sendRedirect("departments");
        } catch (NumberFormatException e) {
            handleError(request, response, "ID de département invalide", department);
        } catch (Exception e) {
            handleDatabaseError(request, response, e, department);
        }
    }


    private void assignEmployeeToDepartment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long employeeId = Long.parseLong(request.getParameter("employeeId"));
        Long departmentId = Long.parseLong(request.getParameter("departmentId"));

        Employee employee = employeeService.getEmployee(employeeId);
        Department department = departmentService.getDepartment(departmentId);

        if (employee != null && department != null) {
            employee.setDepartment(department);
            employeeService.updateEmployee(employee);
            request.setAttribute("message", "L'employé a été assigné au département.");
        } else {
            request.setAttribute("error", "Erreur lors de l'assignation.");
        }

        listDepartments(request, response);
    }



    private void handleDatabaseError(HttpServletRequest request, HttpServletResponse response,
                                     Exception e, Department department)
            throws ServletException, IOException {
        String rootMsg = getRootCauseMessage(e);

        if (rootMsg.contains("constraint")) {
            if (rootMsg.contains("nom")) {
                handleError(request, response, "Ce nom de département est déjà utilisé", department);
            } else if (rootMsg.contains("foreign key")) {
                handleError(request, response, "Impossible de supprimer: département utilisé par des employés", department);
            } else {
                handleError(request, response, "Violation de contrainte: " + rootMsg, department);
            }
        } else {
            handleError(request, response, "Erreur de base de données: " + rootMsg, department);
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String message, Department department)
            throws ServletException, IOException {
        if (department == null) {
            department = new Department();
        }

        request.setAttribute("error", message);
        request.setAttribute("department", department);

        if (department.getId() == null) {
            request.getRequestDispatcher("/WEB-INF/views/department-form.jsp").forward(request, response);
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
