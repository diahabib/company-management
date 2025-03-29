package com.app.management.companymanagement.controller;

import com.app.management.companymanagement.model.Role;
import com.app.management.companymanagement.model.User;
import com.app.management.companymanagement.services.UserService;
import jakarta.ejb.EJB;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/login",
            "/logout",
            "/resources/",
            "/error",
            "/javax.faces.resource/"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Vérifier si la ressource est publique
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(path::startsWith);

        if (isPublic) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        if (!isLoggedIn) {
            // Stocker la page demandée pour redirection après login
            if (session != null) {
                session.setAttribute("originalRequest", path);
            }
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Laisser passer toutes les requêtes pour les utilisateurs connectés
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
