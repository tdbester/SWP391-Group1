package org.example.talentcenter.controller;

import jakarta.servlet.http.*;
import org.example.talentcenter.dao.AccountRequestDAO;
import org.example.talentcenter.model.Request;

import org.example.talentcenter.dao.AccountRequestDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;

@WebServlet("/StudentApplication")
public class StudentApplicationServlet extends HttpServlet {
    private final AccountRequestDAO dao = new AccountRequestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
