package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.talentcenter.dao.BlogDAO;
import org.example.talentcenter.dto.BlogDto;
import org.example.talentcenter.model.Blog;

import java.io.IOException;
import java.util.List;

@WebServlet("/blogs")
public class BlogServlet extends HttpServlet {

    private BlogDAO blogDAO;

    @Override
    public void init() {
        blogDAO = new BlogDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteBlog(request, response);
                break;
            case "list":
            default:
                listBlogs(request, response);
                break;
        }
    }

    // Handle form submissions for create and update
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // For simplicity, action is sent as hidden input in form
        String action = request.getParameter("action");
        if ("insert".equals(action)) {
            insertBlog(request, response);
        } else if ("update".equals(action)) {
            updateBlog(request, response);
        } else {
            response.sendRedirect("blogs");
        }
    }

    private void listBlogs(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<BlogDto> list = blogDAO.getAll();
        request.setAttribute("blogList", list);
        request.getRequestDispatcher("blog.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("blog-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Blog existingBlog = blogDAO.getById(id);
        if (existingBlog == null) {
            response.sendRedirect("blogs");
            return;
        }
        request.setAttribute("blog", existingBlog);
        request.getRequestDispatcher("blog-form.jsp").forward(request, response);
    }

    private void insertBlog(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String statusParam = request.getParameter("status");
        boolean status = (statusParam != null);
        int createdBy = Integer.parseInt(request.getParameter("createdBy"));
        String image = request.getParameter("image");

        Blog newBlog = new Blog();
        newBlog.setTitle(title);
        newBlog.setContent(content);
        newBlog.setStatus(status);
        newBlog.setCreatedBy(createdBy);
        newBlog.setImage(image);
        // created_at set in DAO as current date

        blogDAO.insert(newBlog);
        response.sendRedirect("blogs");
    }

    private void updateBlog(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        boolean status = "on".equals(request.getParameter("status"));
        int createdBy = Integer.parseInt(request.getParameter("createdBy"));
        String image = request.getParameter("image");


        Blog blog = new Blog();
        blog.setId(id);
        blog.setTitle(title);
        blog.setContent(content);
        blog.setStatus(status);
        blog.setImage(image);
        blog.setCreatedBy(createdBy);
        // updated_at set in DAO by getutcdate()

        blogDAO.update(blog);
        response.sendRedirect("blogs");
    }

    private void deleteBlog(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        blogDAO.delete(id);
        response.sendRedirect("blogs");
    }
}
