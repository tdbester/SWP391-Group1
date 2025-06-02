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
import java.util.Date;

@WebServlet("/blogs")
public class BlogServlet extends HttpServlet {
    private BlogDAO blogDAO;

    @Override
    public void init() {
        blogDAO = new BlogDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
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
                deleteBlog(request, response);
                break;
            default:
                listBlogs(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        System.out.println(action);

        switch (action) {
            case "insert":
                insertBlog(request, response);
                break;
            case "update":
                updateBlog(request, response);
                break;
            default:
                response.sendRedirect("blogs");
                break;
        }
    }

    private void listBlogs(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<BlogDto> blogs = blogDAO.getAll();
        request.setAttribute("blogList", blogs);
        request.getRequestDispatcher("blog.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("blog-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Blog existingBlog = blogDAO.getById(id);
        request.setAttribute("blog", existingBlog);
        request.getRequestDispatcher("blog-form.jsp").forward(request, response);
    }

    private void insertBlog(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String content = request.getParameter("content");
        String image = request.getParameter("image");
        Integer authorId = Integer.parseInt(request.getParameter("authorId"));

        Blog newBlog = new Blog();
        newBlog.setTitle(title);
        newBlog.setDescription(description);
        newBlog.setContent(content);
        newBlog.setImage(image);
        newBlog.setAuthorId(authorId);
        newBlog.setCreatedAt(new Date());
        System.out.println(newBlog.getTitle() + " " + newBlog.getDescription());
        blogDAO.insert(newBlog);
        response.sendRedirect("blogs");
    }

    private void updateBlog(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String content = request.getParameter("content");
        String image = request.getParameter("image");
        Integer authorId = Integer.parseInt(request.getParameter("authorId"));

        Blog blog = new Blog();
        blog.setId(id);
        blog.setTitle(title);
        blog.setDescription(description);
        blog.setContent(content);
        blog.setImage(image);
        blog.setAuthorId(authorId);
        blog.setCreatedAt(new Date());

        blogDAO.update(blog);
        response.sendRedirect("blogs");
    }

    private void deleteBlog(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        blogDAO.delete(id);
        response.sendRedirect("blogs");
    }
}