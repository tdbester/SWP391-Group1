package org.example.talentcenter.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.talentcenter.dao.CategoryDAO;
import org.example.talentcenter.dao.CourseDAO;
import org.example.talentcenter.dto.CourseDto;
import org.example.talentcenter.model.Course;
import org.example.talentcenter.model.Category;
import org.example.talentcenter.utilities.Level;
import org.example.talentcenter.utilities.Type;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.text.DecimalFormat;

import static org.example.talentcenter.utilities.Const.TYPE_COURSE;

@WebServlet("/courses")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,    // 2MB
        maxFileSize        = 1024 * 1024 * 10,  // 10MB
        maxRequestSize     = 1024 * 1024 * 50   // 50MB
)
public class CourseServlet extends HttpServlet {
    private CourseDAO courseDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        courseDAO    = new CourseDAO();
        categoryDAO  = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String action = req.getParameter("action");
        if (action == null) action = "list";

        // For public actions like "view", skip session check
        if (!"view".equals(action)) {
            if (session == null || session.getAttribute("accountId") == null) {
                resp.sendRedirect("login");
                return;
            }
        }

        String role = session != null ? (String) session.getAttribute("userRole") : null;

        switch (action) {
            case "new":
                if (!hasRequiredRole(role, "Training Manager")) {
                    resp.sendRedirect("login");
                    return;
                }
                showNewCourseForm(req, resp);
                break;
            case "edit":
                if (!hasRequiredRole(role, "Training Manager")) {
                    resp.sendRedirect("login");
                    return;
                }
                showEditCourseForm(req, resp);
                break;
            case "delete":
                if (!hasRequiredRole(role, "Training Manager")) {
                    resp.sendRedirect("login");
                    return;
                }
                deleteCourse(req, resp);
                break;
            case "view":
                showCourseDetail(req, resp);
                break;
            default:
                if (!hasRequiredRole(role, "Training Manager")) {
                    resp.sendRedirect("login");
                    return;
                }
                listCourses(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Always require authentication for POST operations
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            resp.sendRedirect("login");
            return;
        }

        String role = (String) session.getAttribute("userRole");
        if (!hasRequiredRole(role, "Training Manager")) {
            resp.sendRedirect("login");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "insert": insertCourse(req, resp); break;
            case "update": updateCourse(req, resp); break;
            default:        resp.sendRedirect("courses"); break;
        }
    }

    private void listCourses(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String search = req.getParameter("search");
        String catParam = req.getParameter("category");
        String levelParam = req.getParameter("level");

        // Parse category parameter
        Integer categoryId = null;
        if (catParam != null && !catParam.isBlank()) {
            try {
                categoryId = Integer.parseInt(catParam);
            } catch (NumberFormatException e) {
                req.setAttribute("errorMessage", "Invalid category parameter.");
            }
        }

        // Parse page index
        int index = 1;
        try {
            String indexParam = req.getParameter("index");
            if (indexParam != null) {
                index = Integer.parseInt(indexParam);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Invalid page number.");
        }

        try {
            List<CourseDto> courseList = courseDAO.pagingCourseWithFilters(index, search, categoryId, levelParam);
            int total = courseDAO.getTotalCourseWithFilters(search, categoryId, levelParam);
            int endPage = (int) Math.ceil((double) total / 10);

            req.setAttribute("courseList", courseList);
            req.setAttribute("endP", endPage);
            req.setAttribute("currentIndex", index);
            req.setAttribute("categories", categoryDAO.getByType(TYPE_COURSE));
            req.setAttribute("selectedCategory", categoryId);
            req.setAttribute("selectedLevel", levelParam);

            // Check for success/error messages from previous operations
            String successMessage = req.getParameter("success");
            String errorMessage = req.getParameter("error");

            if (successMessage != null) {
                switch (successMessage) {
                    case "created":
                        req.setAttribute("successMessage", "Khóa học đã được tạo thành công!");
                        break;
                    case "updated":
                        req.setAttribute("successMessage", "Khóa học đã được cập nhật thành công!");
                        break;
                    case "deleted":
                        req.setAttribute("successMessage", "Khóa học đã được xóa thành công!");
                        break;
                }
            }

            if (errorMessage != null) {
                switch (errorMessage) {
                    case "CourseNotFound":
                        req.setAttribute("errorMessage", "Không tìm thấy khóa học!");
                        break;
                    case "InvalidID":
                        req.setAttribute("errorMessage", "ID khóa học không hợp lệ!");
                        break;
                    case "DeleteFailed":
                        req.setAttribute("errorMessage", "Không thể xóa khóa học!");
                        break;
                }
            }

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Có lỗi xảy ra khi tải danh sách khóa học: " + e.getMessage());
            // Set empty list to prevent JSP errors
            req.setAttribute("courseList", new ArrayList<CourseDto>());
            req.setAttribute("endP", 0);
            req.setAttribute("currentIndex", 1);
        }

        req.getRequestDispatcher("/View/course.jsp").forward(req, resp);
    }

    private void showNewCourseForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("categories", categoryDAO.getByType (TYPE_COURSE));
        req.getRequestDispatcher("/View/course-form.jsp").forward(req, resp);
    }

    private void showEditCourseForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Course c = courseDAO.getById(id);
            if (c != null) {
                req.setAttribute("course", c);
                req.setAttribute("categories", categoryDAO.getByType (TYPE_COURSE));
                req.getRequestDispatcher("/View/course-form.jsp").forward(req, resp);
            } else {
                resp.sendRedirect("courses?error=CourseNotFound");
            }
        } catch (NumberFormatException ex) {
            resp.sendRedirect("courses?error=InvalidID");
        }
    }

    private void showCourseDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Course c = courseDAO.getById(id);
            if (c != null) {
                req.setAttribute("course", c);
                req.getRequestDispatcher("/View/user-course-detail.jsp")
                        .forward(req, resp);
            } else {
                resp.sendRedirect("courses?error=CourseNotFound");
            }
        } catch (NumberFormatException ex) {
            resp.sendRedirect("courses?error=InvalidID");
        }
    }

    private void insertCourse(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String title       = req.getParameter("title");
        String info        = req.getParameter("information");
        String catParam    = req.getParameter("category");
        String levelParam  = req.getParameter("level");
        String typeParam   = req.getParameter("type");
        String statusParam = req.getParameter("status");
        Part   imagePart   = req.getPart("imageFile");
        HttpSession session = req.getSession(false);
        int createdBy= (int) session.getAttribute("accountId");

        double price;
        try {
            price = Double.parseDouble(req.getParameter("price"));
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Invalid price format.");
            showNewCourseForm(req, resp);
            return;
        }

        String imageUrl;

        //3. lấy ảnh và upload ln cloudinary, lấy về URL ảnh để lưu vào database
        if (imagePart != null && imagePart.getSize() > 0) {
            imageUrl = uploadToCloudinary(imagePart);
        } else {
            imageUrl = "https://placehold.co/600x400?text=img";
        }
        Category category;
        try {
            category = categoryDAO.getById(Integer.parseInt(catParam));
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Invalid category.");
            showNewCourseForm(req, resp);
            return;
        }

        // Parse Level enum
        Level level = null;
        if (levelParam != null && !levelParam.isBlank()) {
            try {
                level = Level.valueOf(levelParam);
            } catch (IllegalArgumentException e) {
                req.setAttribute("errorMessage", "Invalid level.");
                showNewCourseForm(req, resp);
                return;
            }
        }

        // Parse Type enum
        Type type = null;
        if (typeParam != null && !typeParam.isBlank()) {
            try {
                type = Type.valueOf(typeParam);
            } catch (IllegalArgumentException e) {
                req.setAttribute("errorMessage", "Invalid type.");
                showNewCourseForm(req, resp);
                return;
            }
        }

        // Parse status parameter
        int status = 1; // Default to public
        if (statusParam != null && !statusParam.isBlank()) {
            try {
                status = Integer.parseInt(statusParam);
            } catch (NumberFormatException e) {
                // Keep default value
            }
        }

        Course c = new Course();
        c.setTitle(title);
        c.setInformation(info);
        c.setPrice(price);
        c.setCreatedBy(createdBy);
        c.setImage(imageUrl);
        c.setCategory(category);
        c.setLevel(level);
        c.setType(type);
        c.setStatus(status);

        try {
            courseDAO.insert(c);
            resp.sendRedirect("courses?success=created");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Có lỗi xảy ra khi tạo khóa học: " + e.getMessage());
            showNewCourseForm(req, resp);
        }
    }

    private void updateCourse(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String title    = req.getParameter("title");
        String info     = req.getParameter("information");
        String catParam = req.getParameter("category");
        String levelParam  = req.getParameter("level");
        String typeParam   = req.getParameter("type");
        String statusParam = req.getParameter("status");
        Part   imagePart= req.getPart("imageFile");
//        int    createdBy;
        HttpSession session = req.getSession(false);
        int createdBy= (int) session.getAttribute("accountId");

        double price;
        try {
            price = Double.parseDouble(req.getParameter("price"));
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Invalid price format.");
            showEditCourseForm(req, resp);
            return;
        }

        String imageUrl = req.getParameter("currentImageUrl");
        if (imagePart != null && imagePart.getSize() > 0) {
            imageUrl = uploadToCloudinary(imagePart);
        }

        Category category;
        try {
            category = categoryDAO.getById(Integer.parseInt(catParam));
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Invalid category.");
            showEditCourseForm(req, resp);
            return;
        }

        // Parse Level enum
        Level level = null;
        if (levelParam != null && !levelParam.isBlank()) {
            try {
                level = Level.valueOf(levelParam);
            } catch (IllegalArgumentException e) {
                req.setAttribute("errorMessage", "Invalid level.");
                showEditCourseForm(req, resp);
                return;
            }
        }

        // Parse Type enum
        Type type = null;
        if (typeParam != null && !typeParam.isBlank()) {
            try {
                type = Type.valueOf(typeParam);
            } catch (IllegalArgumentException e) {
                req.setAttribute("errorMessage", "Invalid type.");
                showEditCourseForm(req, resp);
                return;
            }
        }

        // Parse status parameter
        int status = 1; // Default to public
        if (statusParam != null && !statusParam.isBlank()) {
            try {
                status = Integer.parseInt(statusParam);
            } catch (NumberFormatException e) {
                // Keep default value
            }
        }

        Course c = new Course();
        c.setId(id);
        c.setTitle(title);
        c.setInformation(info);
        c.setPrice(price);
        c.setCreatedBy(createdBy);
        c.setImage(imageUrl);
        c.setCategory(category);
        c.setLevel(level);
        c.setType(type);
        c.setStatus(status);

        try {
            courseDAO.update(c);
            resp.sendRedirect("courses?success=updated");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật khóa học: " + e.getMessage());
            showEditCourseForm(req, resp);
        }
    }

    private void deleteCourse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            courseDAO.delete(id);
            resp.sendRedirect("courses?success=deleted");
        } catch (NumberFormatException e) {
            resp.sendRedirect("courses?error=InvalidID");
        } catch (Exception e) {
            resp.sendRedirect("courses?error=DeleteFailed");
        }
    }

    private boolean hasRequiredRole(String userRole, String requiredRole) {
        return userRole != null && userRole.equalsIgnoreCase(requiredRole);
    }

    private String uploadToCloudinary(Part filePart) throws IOException, ServletException {
        //1. Tạo kết nối web của mình đến cloudinary
        Cloudinary cloudinary = Singleton.getCloudinary();
        if (cloudinary == null) {
            throw new ServletException("Cloudinary not configured");
        }

        //2. Khởi tạo đối tượng file- là file người dùng upload lên
        // ( đối tượng file được lưu trong bộ nhớ hệ thống)
        File tempFile;
        try (InputStream input = filePart.getInputStream()) {
            String fileName  = filePart.getSubmittedFileName();
            String extension = fileName.substring(fileName.lastIndexOf('.'));
            tempFile = File.createTempFile("upload", extension);
            Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        //3. Gửi ảnh lên cloudinary
        Map<?,?> uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());

        //4. xóa đối tượng file tạm trong bộ nhớ hệ thống
        tempFile.delete();

        //5. trả về URL ảnh đã upload
        return (String) uploadResult.get("secure_url");
    }
}
