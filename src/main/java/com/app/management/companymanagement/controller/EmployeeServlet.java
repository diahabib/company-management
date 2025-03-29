package com.app.management.companymanagement.controller;

import com.app.management.companymanagement.helpers.Layout;
import com.app.management.companymanagement.model.Department;
import com.app.management.companymanagement.model.Employee;
import com.app.management.companymanagement.model.Role;
import com.app.management.companymanagement.model.User;
import com.app.management.companymanagement.services.DepartmentService;
import com.app.management.companymanagement.services.EmployeeService;
import com.app.management.companymanagement.services.UserService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.app.management.companymanagement.helpers.Layout.renderWithLayout;

@WebServlet("/employees")
//@ServletSecurity(@HttpConstraint(rolesAllowed = {"ADMIN", "MANAGER"}))
public class EmployeeServlet extends HttpServlet {

    @EJB
    private EmployeeService employeeService;

    @EJB
    private DepartmentService departmentService;

    @EJB
    private UserService userService;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "search": //
                searchEmployees(request, response);
                break;
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteEmployee(request, response);
                break;
            case "view":
                viewEmployee(request, response);
                break;
            default:
                listEmployees(request, response);
                break;
        }

    }

    private void viewEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Employee employee = employeeService.getEmployee(id);

            if (employee == null) {
                request.setAttribute("error", "Employé non trouvé.");
                listEmployees(request, response);
                return;
            }

            request.setAttribute("employee", employee);
            request.getRequestDispatcher("/WEB-INF/views/employee-view.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid employee ID");
            listEmployees(request, response);
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get all departments for the dropdown
        //Employee employee = new Employee();
        //employee.setId(0L);
        List<Department> departments = departmentService.getAllDepartments();
        request.setAttribute("departments", departments);
        //request.setAttribute("employee", employee); // Empty employee object for the form

        request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp").forward(request, response);
    }

    private void listEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Department> departments = departmentService.getAllDepartments();
        request.setAttribute("departments", departments);
        List<Employee> employees = employeeService.getAllEmployees();
        request.setAttribute("employees", employees);
        request.getRequestDispatcher("/WEB-INF/views/employee.jsp").forward(request, response);

        Layout.renderWithLayout(request, response,
                "Gestion des Employés",
                "/WEB-INF/views/employee.jsp");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Employee existingEmployee = employeeService.getEmployee(id);

        List<Department> departments = departmentService.getAllDepartments();

        request.setAttribute("employee", existingEmployee);
        request.setAttribute("departments", departments);
        request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp").forward(request, response);
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Employee employee = employeeService.getEmployee(id);

        if (employee.getUser() != null) {
            userService.deleteUser(employee.getUser().getId());
        }

        employeeService.deleteEmployee(id);
        response.sendRedirect("employees");
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Récupération des paramètres
        String idParam = request.getParameter("id");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email").trim();
        String role = request.getParameter("role");
        String departmentIdParam = request.getParameter("departmentId");

        // Validation de base
        if (nom == null || nom.isEmpty() || prenom == null || prenom.isEmpty() ||
                email == null || email.isEmpty() || role == null || role.isEmpty()) {
            handleError(request, response, "Tous les champs obligatoires doivent être remplis.", null);
            return;
        }


        Employee employee = new Employee(nom, prenom, email, role);

        try {
            // Gestion du département
            if (departmentIdParam != null && !departmentIdParam.isEmpty()) {
                try {
                    Long departmentId = Long.parseLong(departmentIdParam);
                    Department department = departmentService.getDepartment(departmentId);
                    if (department == null) {
                        handleError(request, response, "Le département sélectionné n'existe pas.", employee);
                        return;
                    }
                    employee.setDepartment(department);
                } catch (NumberFormatException e) {
                    handleError(request, response, "ID de département invalide.", employee);
                    return;
                }
            }

            Role.RoleEnum userRole = Role.RoleEnum.valueOf("EMPLOYEE");
            // Création ou mise à jour
            if (idParam == null || idParam.isEmpty()) {
                createNewEmployee(request, response, employee, String.valueOf(userRole));
            } else {
                updateExistingEmployee(request, response, employee, idParam);
            }
        } catch (Exception e) {
            handleError(request, response, "Une erreur technique est survenue. Veuillez réessayer.", employee);
        }
    }

    /*@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String departmentIdParam = request.getParameter("departmentId");

        Employee employee = new Employee(nom, prenom, email, role);
        if (departmentIdParam != null && !departmentIdParam.isEmpty()) {
            Long departmentId = Long.parseLong(departmentIdParam);
            Department department = departmentService.getDepartment(departmentId);
            employee.setDepartment(department);
        }


        if (idParam == null || idParam.isEmpty()) {
            try {
                Role.RoleEnum roleEnum = Role.RoleEnum.valueOf(role.toUpperCase());
                User user = userService.createUserForEmployee(employee, "defaultPassword123", roleEnum);
                employee.setUser(user);
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Rôle invalide.");
                request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                request.setAttribute("error", "Échec de la création du compte utilisateur : " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp").forward(request, response);
                return;
            }
            employeeService.addEmployee(employee);
        } else {
            employee.setId(Long.parseLong(idParam));
            employeeService.updateEmployee(employee);
        }

        response.sendRedirect("employees");
    }*/

    private void createNewEmployee(HttpServletRequest request, HttpServletResponse response,
                                   Employee employee, String role)
            throws ServletException, IOException {

        if (employeeService.emailExists(employee.getEmail())) {
            handleError(request, response, "L'adresse email est déjà utilisée par un autre employé.", employee);
            return;
        }

        try {
            // Validation du rôle
            Role.RoleEnum roleEnum;
            try {
                roleEnum = Role.RoleEnum.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                handleError(request, response, "Rôle invalide.", employee);
                return;
            }

            // Création de l'utilisateur
            User user;
            try {
                user = userService.createUserForEmployee(employee, "defaultPassword123", roleEnum);
            } catch (Exception e) {
                handleError(request, response, "Échec de la création du compte utilisateur: " +
                        getRootCauseMessage(e), employee);
                return;
            }
            employee.setUser(user);

            try {
                employeeService.addEmployee(employee);
                response.sendRedirect("employees");
            } catch (Exception e) {
                handleDatabaseError(request, response, e, employee);
            }
        } catch (Exception e) {
            handleError(request, response, "Erreur lors de la création de l'employé", employee);
        }
    }

    private void updateExistingEmployee(HttpServletRequest request, HttpServletResponse response,
                                        Employee employee, String idParam)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(idParam);
            employee.setId(id);

            // Vérifier si l'employé existe
            Employee existing = employeeService.getEmployee(id);
            if (existing == null) {
                handleError(request, response, "L'employé à modifier n'existe pas.", employee);
                return;
            }

            // Mise à jour
            try {
                employeeService.updateEmployee(employee);
                response.sendRedirect("employees");
            } catch (Exception e) {
                handleDatabaseError(request, response, e, employee);
            }
        } catch (NumberFormatException e) {
            handleError(request, response, "ID d'employé invalide.", employee);
        }
    }

    private void handleDatabaseError(HttpServletRequest request, HttpServletResponse response,
                                     Exception e, Employee employee)
            throws ServletException, IOException {
        // Vérifier les contraintes de base de données
        String rootMsg = getRootCauseMessage(e);

        if (rootMsg.contains("employee_email_key")) {
            handleError(request, response, "L'adresse email est déjà utilisée par un autre employé.", employee);
        } else if (rootMsg.contains("foreign key constraint")) {
            handleError(request, response, "Opération impossible: référence à un élément inexistant.", employee);
        } else {
            handleError(request, response, "Erreur de base de données: " + rootMsg, employee);
        }
    }

    private String getRootCauseMessage(Throwable e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             String message, Employee employee)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.setAttribute("employee", employee);
        request.setAttribute("departments", departmentService.getAllDepartments());
        request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp").forward(request, response);
    }

    private void searchEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String term = request.getParameter("term");
        String departmentId = request.getParameter("departmentId");
        String role = request.getParameter("role");

        List<Employee> employees = employeeService.searchEmployees(term, departmentId, role);
        List<Department> departments = departmentService.getAllDepartments();

        request.setAttribute("employees", employees);
        request.setAttribute("departments", departments);
        request.setAttribute("searchTerm", term);  // For repopulating the search form
        request.setAttribute("selectedDepartment", departmentId);  // For keeping selection
        request.setAttribute("selectedRole", role);
        request.getRequestDispatcher("/WEB-INF/views/employee.jsp").forward(request, response);
    }



}
