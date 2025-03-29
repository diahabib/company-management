package com.app.management.companymanagement.helpers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Layout {
    public static void renderWithLayout(HttpServletRequest request,
                                        HttpServletResponse response,
                                        String pageTitle,
                                        String contentPage)
            throws ServletException, IOException {

        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("contentPage", contentPage);

        String activePage = contentPage.replace("/WEB-INF/views/", "")
                .replace(".jsp", "")
                .replace("-form", "")
                .replace("-view", "");
        request.setAttribute("activePage", activePage);

        request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
    }
}
