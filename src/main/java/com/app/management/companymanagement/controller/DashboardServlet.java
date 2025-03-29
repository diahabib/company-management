package com.app.management.companymanagement.controller;

import com.app.management.companymanagement.model.Role;
import com.app.management.companymanagement.model.User;
import com.app.management.companymanagement.services.DepartmentService;
import com.app.management.companymanagement.services.EmployeeService;
import com.app.management.companymanagement.services.ProjectService;
import com.app.management.companymanagement.services.UserService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.app.management.companymanagement.helpers.Layout.renderWithLayout;

@WebServlet("/dashboard")
//@ServletSecurity(@HttpConstraint(rolesAllowed = {"ADMIN"}))
public class DashboardServlet extends HttpServlet {

    @EJB
    private EmployeeService employeeService;

    @EJB
    private DepartmentService departmentService;

    @EJB
    private ProjectService projectService;

    @EJB
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");


        if (userService.hasRole(user, Role.RoleEnum.ADMIN)) {
            request.setAttribute("employeeCount", employeeService.countEmployees());
            request.setAttribute("departmentCount", departmentService.countDepartments());
            request.setAttribute("projectCount", projectService.countProjects());
        }

        renderWithLayout(request, response,
                "Tableau de bord",
                "/WEB-INF/views/dashboard.jsp");
    }
}