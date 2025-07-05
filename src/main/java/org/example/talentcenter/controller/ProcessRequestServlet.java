package org.example.talentcenter.controller;

import org.example.talentcenter.dao.RequestDAO;
import org.example.talentcenter.model.Account;
import org.example.talentcenter.model.Request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ProcessRequestServlet", value = "/ProcessRequest")
public class ProcessRequestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect("View/login.jsp");
            return;
        }
        if ("list".equals(action)) {
            RequestDAO requestDAO = new RequestDAO();
            ArrayList<Request> requestList = requestDAO.getAllRequest();
            request.setAttribute("requestList", requestList);
            request.getRequestDispatcher("/View/manager-request-list.jsp").forward(request, response);
        }
    }
}
