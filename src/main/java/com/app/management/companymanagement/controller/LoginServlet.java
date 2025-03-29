package com.app.management.companymanagement.controller;

import com.app.management.companymanagement.model.Role;
import com.app.management.companymanagement.model.User;
import com.app.management.companymanagement.services.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Inject
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Si déjà connecté, rediriger vers le dashboard
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User authenticatedUser = userService.authenticate(username, password);

            // Invalider l'ancienne session et en créer une nouvelle
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("user", authenticatedUser);
            newSession.setMaxInactiveInterval(30 * 60); // 30 minutes

            // Rediriger vers la page d'origine ou le dashboard
            String originalRequest = (String) newSession.getAttribute("originalRequest");
            if (originalRequest != null && !originalRequest.isEmpty()) {
                newSession.removeAttribute("originalRequest");
                response.sendRedirect(request.getContextPath() + originalRequest);
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }

        } catch (AuthenticationException e) {
            request.setAttribute("errorMessage", "Identifiants invalides");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}