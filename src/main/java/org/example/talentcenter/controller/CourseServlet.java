package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.talentcenter.dao.CourseDAO; // Import CourseDAO
import org.example.talentcenter.dto.CourseDto; // Import CourseDto
import org.example.talentcenter.model.Course; // Import Course model
import org.example.talentcenter.utilities.CourseCategory; // Import your CourseCategory enum

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map; // For Cloudinary upload result

import jakarta.servlet.annotation.MultipartConfig;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;


@WebServlet("/courses") // Map this servlet to the /courses URL
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 50)    // 50MB
public class CourseServlet extends HttpServlet {
    private CourseDAO courseDAO;

    @Override
    public void init() {
        courseDAO = new CourseDAO();
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
                showNewCourseForm(request, response);
                break;
            case "edit":
                showEditCourseForm(request, response);
                break;
            case "delete":
                deleteCourse(request, response);
                break;
            case "view":
                showCourseDetail(request, response);
                break;
            default:
                listCourses(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        System.out.println("Course Action: " + action); // Log action for debugging

        switch (action) {
            case "insert":
                insertCourse(request, response);
                break;
            case "update":
                updateCourse(request, response);
                break;
            default:
                response.sendRedirect("courses");
                break;
        }
    }

    private void listCourses(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String search = request.getParameter("search");
        String category = request.getParameter("category");
        final CourseCategory finalSelectedCategory;

        if(category != null && !category.isEmpty()){
            try {
                finalSelectedCategory = CourseCategory.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid CourseCategory received: " + category);
                request.setAttribute("errorMessage", "Invalid course category selected.");
                showNewCourseForm(request, response);
                return;
            }
        } else {
            finalSelectedCategory = null;
        }


        Integer index = 1;
        if (request.getParameter("index") != null) {
            try {
                index = Integer.parseInt(request.getParameter("index"));
            } catch (NumberFormatException e) {
                System.err.println("Invalid index parameter: " + request.getParameter("index"));
                index = 1; // Default to first page
            }
        }

        List<CourseDto> courses = courseDAO.pagingCourse(index);
        List<CourseDto> filteredCourses = courses.stream()
                .filter(course -> search == null || course.getTitle().toLowerCase().contains(search.toLowerCase()))
                .toList();

        if(finalSelectedCategory != null){
            filteredCourses = filteredCourses.stream()
                    .filter(x -> x.getCategory().equals(finalSelectedCategory))
                    .toList();
        }


        for (CourseDto course : filteredCourses) {
            System.out.println(course.getCategory());
        }

        request.setAttribute("courseList", filteredCourses);
        int totalCourses = courseDAO.getTotalCourse();
        int endPage = (int) Math.ceil((double) totalCourses / 5); // 5 courses per page
        request.setAttribute("endP", endPage);
        request.setAttribute("categories", CourseCategory.values());
        request.setAttribute("currentIndex", index); // To highlight current page in pagination

        request.getRequestDispatcher("./View/course.jsp").forward(request, response);
    }

    private void showNewCourseForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Pass all enum values to the JSP for dropdown
        request.setAttribute("categories", CourseCategory.values());
        request.getRequestDispatcher("./View/course-form.jsp").forward(request, response);
    }

    private void showCourseDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Course course = courseDAO.getById(id);
            if (course != null) {
                request.setAttribute("course", course);
                request.getRequestDispatcher("./View/user-course-detail.jsp").forward(request, response);
            } else {
                response.sendRedirect("courses?error=CourseNotFound");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID parameter for course detail: " + e.getMessage());
            response.sendRedirect("courses?error=InvalidID");
        }
    }


    private void showEditCourseForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Course existingCourse = courseDAO.getById(id);
            if (existingCourse != null) {
                request.setAttribute("course", existingCourse);
                // Pass all enum values to the JSP for dropdown
                request.setAttribute("categories", CourseCategory.values());
                request.getRequestDispatcher("./View/course-form.jsp").forward(request, response);
            } else {
                response.sendRedirect("courses?error=CourseNotFound");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID parameter for course edit: " + e.getMessage());
            response.sendRedirect("courses?error=InvalidID");
        }
    }

    private void insertCourse(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String title = request.getParameter("title");
        double price = 0.0;
        try {
            price = Double.parseDouble(request.getParameter("price"));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid price format.");
            showNewCourseForm(request, response);
            return;
        }
        String information = request.getParameter("information");
        Part imagePart = request.getPart("imageFile"); // Name of the file input in the form
        String categoryString = request.getParameter("category");

        // Ensure that createdBy is obtained from the authenticated user's session in a real app
        // For simplicity, I'm parsing it from request parameter here, but it should be from session.
        Integer createdBy = 0;
        try {
            createdBy = Integer.parseInt(request.getParameter("createdBy"));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid createdBy ID format.");
            showNewCourseForm(request, response);
            return;
        }

        String imageUrl = null;

        if (imagePart != null && imagePart.getSize() > 0) {
            try {
                imageUrl = uploadToCloudinary(imagePart);
            } catch (Exception e) {
                System.err.println("Error uploading image to Cloudinary: " + e.getMessage());
                request.setAttribute("errorMessage", "Failed to upload image. Please try again.");
                showNewCourseForm(request, response);
                return;
            }
        } else {
            System.out.println("No image file provided for course insertion.");
            // Set a default image URL if no image is uploaded
            imageUrl = "https://res.cloudinary.com/your-cloud-name/image/upload/v1/default_course_image.png"; // Replace with your actual default image URL
        }

        CourseCategory category = null;
        if (categoryString != null && !categoryString.isEmpty()) {
            try {
                category = CourseCategory.valueOf(categoryString.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid CourseCategory received: " + categoryString);
                request.setAttribute("errorMessage", "Invalid course category selected.");
                showNewCourseForm(request, response);
                return;
            }
        } else {
            request.setAttribute("errorMessage", "Course category cannot be empty.");
            showNewCourseForm(request, response);
            return;
        }

        Course newCourse = new Course();
        newCourse.setTitle(title);
        newCourse.setPrice(price);
        newCourse.setInformation(information);
        newCourse.setCreatedBy(createdBy);
        newCourse.setImage(imageUrl);
        newCourse.setCategory(category); // Set the enum value

        System.out.println("Inserting Course: " + newCourse.getTitle() + ", Category: " + newCourse.getCategory() + ", Image URL: " + newCourse.getImage());
        courseDAO.insert(newCourse);
        response.sendRedirect("courses");
    }

    private void updateCourse(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        double price = 0.0;
        try {
            price = Double.parseDouble(request.getParameter("price"));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid price format.");
            showEditCourseForm(request, response); // Go back to edit form
            return;
        }
        String information = request.getParameter("information");
        Part imagePart = request.getPart("imageFile");
        String categoryString = request.getParameter("category");

        Integer createdBy = 0; // Again, should be from session in a real app
        try {
            createdBy = Integer.parseInt(request.getParameter("createdBy"));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid createdBy ID format.");
            showEditCourseForm(request, response);
            return;
        }

        String imageUrl = request.getParameter("currentImageUrl"); // Get current image URL from a hidden input

        // Check if a new image is uploaded
        if (imagePart != null && imagePart.getSize() > 0) {
            try {
                imageUrl = uploadToCloudinary(imagePart);
            } catch (Exception e) {
                System.err.println("Error uploading new image to Cloudinary: " + e.getMessage());
                request.setAttribute("errorMessage", "Failed to upload new image. Please try again.");
                showEditCourseForm(request, response); // Go back to edit form with error
                return;
            }
        } else {
            System.out.println("No new image file provided for update. Keeping existing image.");
        }

        CourseCategory category = null;
        if (categoryString != null && !categoryString.isEmpty()) {
            try {
                category = CourseCategory.valueOf(categoryString.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid CourseCategory received: " + categoryString);
                request.setAttribute("errorMessage", "Invalid course category selected.");
                showEditCourseForm(request, response);
                return;
            }
        } else {
            request.setAttribute("errorMessage", "Course category cannot be empty.");
            showEditCourseForm(request, response);
            return;
        }

        Course course = new Course();
        course.setId(id);
        course.setTitle(title);
        course.setPrice(price);
        course.setInformation(information);
        course.setCreatedBy(createdBy);
        course.setImage(imageUrl);
        course.setCategory(category); // Set the enum value

        System.out.println("Updating Course: " + course.getTitle() + ", Category: " + course.getCategory() + ", Image URL: " + course.getImage());
        courseDAO.update(course);
        response.sendRedirect("courses");
    }

    private void deleteCourse(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            courseDAO.delete(id);
            response.sendRedirect("courses");
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID parameter for course delete: " + e.getMessage());
            response.sendRedirect("courses?error=InvalidIDForDelete");
        }
    }

    private String uploadToCloudinary(Part filePart) throws IOException, ServletException {
        // Step 1: Initialize Cloudinary object
        Cloudinary cloudinary = Singleton.getCloudinary();
        if (cloudinary == null) {
            throw new ServletException("Cloudinary configuration error: Singleton not initialized.");
        }

        // Step 2: Read the uploaded file into a temporary file
        File tempFile = null;
        try (InputStream input = filePart.getInputStream()) {
            String fileName = filePart.getSubmittedFileName();
            String extension = "";
            int i = fileName.lastIndexOf('.');
            if (i > 0) {
                extension = fileName.substring(i);
            }
            tempFile = File.createTempFile("upload", extension);
            Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw e; // Re-throw the exception for handling upstream
        }

        // Step 3: Upload the image to Cloudinary
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());
        } catch (IOException e) {
            System.err.println("Error uploading to Cloudinary: " + e.getMessage());
            throw e; // Re-throw the exception for handling upstream
        } finally {
            // Ensure the temporary file is deleted
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }

        // Step 4: Get the secure URL of the uploaded image
        return (String) uploadResult.get("secure_url");
    }
}

