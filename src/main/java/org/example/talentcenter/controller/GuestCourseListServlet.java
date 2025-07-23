package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.talentcenter.dao.CourseDAO;
import org.example.talentcenter.dao.CategoryDAO;
import org.example.talentcenter.dto.CourseDto;
import org.example.talentcenter.model.Category;
import org.example.talentcenter.model.Course;
import org.example.talentcenter.utilities.Level;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.talentcenter.utilities.Const.TYPE_COURSE;

@WebServlet(name = "guestCourseList", value = "/guest-courses")
public class GuestCourseListServlet extends HttpServlet {
    private CourseDAO courseDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        courseDAO = new CourseDAO();
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get search parameters
        String search = request.getParameter("search");
        String catParam = request.getParameter("category");
        String levelParam = request.getParameter("level");
        
        // Get only public courses (status = 1)
        List<CourseDto> publicCourses = courseDAO.getPublicCoursesDto();
        
        // Apply search filter if provided
        List<CourseDto> filteredCourses = publicCourses;
        if (search != null && !search.trim().isEmpty()) {
            filteredCourses = publicCourses.stream()
                    .filter(course -> course.getTitle().toLowerCase().contains(search.toLowerCase()) ||
                                    course.getInformation().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        // Apply category filter if provided
        Category filterCategory = null;
        if (catParam != null && !catParam.isBlank()) {
            try {
                int categoryId = Integer.parseInt(catParam);
                filterCategory = categoryDAO.getById(categoryId);
                if (filterCategory != null) {
                    filteredCourses = filteredCourses.stream()
                            .filter(course -> course.getCategory().getId() == categoryId)
                            .collect(Collectors.toList());
                }
            } catch (NumberFormatException e) {
                // Invalid category parameter, ignore filter
            }
        }
        
        // Apply level filter if provided
        Level filterLevel = null;
        if (levelParam != null && !levelParam.isBlank()) {
            try {
                filterLevel = Level.valueOf(levelParam);
                final Level finalFilterLevel = filterLevel;
                filteredCourses = filteredCourses.stream()
                        .filter(course -> course.getLevel() == finalFilterLevel)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                // Invalid level parameter, ignore filter
            }
        }
        
        // Get all course categories for filter dropdown
        List<Category> categories = categoryDAO.getByType(TYPE_COURSE);
        
        // Set attributes for JSP
        request.setAttribute("courseList", filteredCourses);
        request.setAttribute("categories", categories);
        request.setAttribute("selectedCategory", filterCategory != null ? filterCategory.getId() : null);
        request.setAttribute("selectedLevel", levelParam);
        request.setAttribute("searchQuery", search);
        
        // Forward to guest course list JSP
        request.getRequestDispatcher("/View/guest-course-list.jsp").forward(request, response);
    }
}
