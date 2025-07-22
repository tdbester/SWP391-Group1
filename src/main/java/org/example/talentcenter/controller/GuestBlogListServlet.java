package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.talentcenter.dao.BlogDAO;
import org.example.talentcenter.dao.CategoryDAO;
import org.example.talentcenter.dto.BlogDto;
import org.example.talentcenter.model.Category;

import java.io.IOException;
import java.util.List;

import static org.example.talentcenter.utilities.Const.TYPE_BLOG;

@WebServlet(name = "guestBlogList", value = "/guest-blogs")
public class GuestBlogListServlet extends HttpServlet {
    private BlogDAO blogDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        blogDAO = new BlogDAO();
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get search parameters
        String search = request.getParameter("search");
        String catParam = request.getParameter("category");
        
        // Get only public blogs (status = 1)
        List<BlogDto> publicBlogs = blogDAO.getPublicBlogs();
        
        // Apply search filter if provided
        List<BlogDto> filteredBlogs = publicBlogs;
        if (search != null && !search.trim().isEmpty()) {
            filteredBlogs = publicBlogs.stream()
                    .filter(blog -> blog.getTitle().toLowerCase().contains(search.toLowerCase()) ||
                                  blog.getDescription().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }
        
        // Apply category filter if provided
        Category filterCategory = null;
        if (catParam != null && !catParam.isBlank()) {
            try {
                int categoryId = Integer.parseInt(catParam);
                filterCategory = categoryDAO.getById(categoryId);
                if (filterCategory != null) {
                    filteredBlogs = filteredBlogs.stream()
                            .filter(blog -> blog.getCategory() == categoryId)
                            .toList();
                }
            } catch (NumberFormatException e) {
                // Invalid category parameter, ignore filter
            }
        }
        
        // Get all blog categories for filter dropdown
        List<Category> categories = categoryDAO.getByType(TYPE_BLOG);
        
        // Set attributes for JSP
        request.setAttribute("blogList", filteredBlogs);
        request.setAttribute("categories", categories);
        request.setAttribute("selectedCategory", filterCategory != null ? filterCategory.getId() : null);
        request.setAttribute("searchQuery", search);
        
        // Forward to guest blog list JSP
        request.getRequestDispatcher("/View/guest-blog-list.jsp").forward(request, response);
    }
}
