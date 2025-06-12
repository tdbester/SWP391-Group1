//package org.example.talentcenter.controller;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.Singleton;
//import com.cloudinary.utils.ObjectUtils;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.Part;
//import org.example.talentcenter.dao.BlogDAO;
//import org.example.talentcenter.dao.CourseDAO;
//import org.example.talentcenter.dto.BlogDto;
//import org.example.talentcenter.model.Blog;
//import org.example.talentcenter.model.Course;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.StandardCopyOption;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//@WebServlet("/courses")
//public class CourseServlet extends HttpServlet {
//    private CourseDAO courseDAO;
//
//
//    @Override
//    public void init() {
//        courseDAO = new CourseDAO();
//    }
//    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws jakarta.servlet.ServletException, java.io.IOException {
//        String action = request.getParameter("action");
//        if (action == null) {
//            action = "list";
//        }
//
//
//        switch (action) {
//            case "new":
//                showNewCourseForm(request, response);
//                break;
//            case "edit":
//                showEditCourseForm(request, response);
//                break;
//            case "delete":
//                deleteCourse(request, response);
//                break;
//            default:
//                listCourses(request, response);
//                break;
//        }
//    }
//    protected void doPost(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws jakarta.servlet.ServletException, java.io.IOException {
//        String action = request.getParameter("action");
//        if (action == null) {
//            action = "list";
//        }
//        System.out.println("Action: " + action); // Log action for debugging
//
//
//        switch (action) {
//            case "insert":
//                insertCourse(request, response);
//                break;
//            case "update":
//                updateCourse(request, response);
//                break;
//            default:
//                response.sendRedirect("courses");
//                break;
//        }
//    }
//    private void listCourses(HttpServletRequest request, HttpServletResponse response)
//            throws IOException, ServletException {
//        String search = request.getParameter("search");
//        Integer index = 1;
//        if (request.getParameter("index") != null) {
//            index = Integer.parseInt(request.getParameter("index"));
//        }
//        List<CourseDto> courses = courseDAO.pagingCourse(index);
//        List<CourseDto> filtered = courses.stream().filter(course ->
//                search == null || course.getTitle().toLowerCase().contains(search.toLowerCase())).toList();
//        request.setAttribute("courseList", filtered);
//        CourseDAO courseDAO = new CourseDAO();
//        int count = courseDAO.getTotalCourse();
//        int endPage = count % 5 == 0 ? count / 5 : count / 5 + 1;
//        request.setAttribute("endP", endPage);
//        request.getRequestDispatcher("./View/course.jsp").forward(request, response);
//    }
//    private void showNewCourseForm(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        request.getRequestDispatcher("./View/course-form.jsp").forward(request, response);
//    }
//    private void showEditCourseForm(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        int id = Integer.parseInt(request.getParameter("id"));
//        Course existingCourse = courseDAO.getById(id);
//        request.setAttribute("course", existingCourse);
//        request.getRequestDispatcher("./View/course-form.jsp").forward(request, response);
//    }
//    private void insertCourse(HttpServletRequest request, HttpServletResponse response)
//            throws IOException, ServletException {
//        String title = request.getParameter("title");
//        String description = request.getParameter("description");
//        String content = request.getParameter("content");
//        Part imagePart = request.getPart("imageFile"); // Tên của input type="file" trong form
//
//
//        String imageUrl = null; // Khởi tạo null để dễ kiểm tra
//
//
//        if (imagePart != null && imagePart.getSize() > 0) {
//            try {
//                imageUrl = uploadToCloudinary(imagePart);
//            } catch (Exception e) {
//                // Xử lý lỗi upload ảnh
//                System.err.println("Error uploading image to Cloudinary: " + e.getMessage());
//                request.setAttribute("errorMessage", "Failed to upload image. Please try again.");
//                // Chuyển hướng về form hoặc hiển thị lỗi
//                showNewCourseForm(request, response);
//                return;
//            }
//        } else {
//            // Nếu không có file ảnh được upload, bạn có thể đặt một URL ảnh mặc định hoặc để trống
//            System.out.println("No image file provided for insertion.");
//            imageUrl = "https://res.cloudinary.com/your-cloud-name/image/upload/v1/default_image.png"; // Thay bằng ảnh mặc định của bạn nếu có
//        }
//
//
//        Integer authorId = Integer.parseInt(request.getParameter("authorId"));
//
//
//        Blog newBlog = new Blog();
//        newBlog.setTitle(title);
//        newBlog.setDescription(description);
//        newBlog.setContent(content);
//        newBlog.setImage(imageUrl);
//        newBlog.setAuthorId(authorId);
//        newBlog.setCreatedAt(new Date());
//
//        System.out.println("Inserting Blog: " + newBlog.getTitle() + ", Image URL: " + newBlog.getImage());
//        blogDAO.insert(newBlog);
//        response.sendRedirect("blogs");
//    }
//    private void deleteCourse(HttpServletRequest request, HttpServletResponse response)
//            throws IOException {
//        int id = Integer.parseInt(request.getParameter("id"));
//        courseDAO.delete(id);
//        response.sendRedirect("courses");
//    }
//
//
//    private String uploadToCloudinary(Part filePart) throws IOException, ServletException {
//        // step 1: khởi tạo 1 đối tượng để có thể call đến cloudinary
//        Cloudinary cloudinary = Singleton.getCloudinary();
//        if (cloudinary == null) {
//            throw new ServletException("Error");
//        }
//
//        // step 2: đọc file mà người dùng upload lên
//        File tempFile = null;
//        try (InputStream input = filePart.getInputStream()) {
//            String fileName = filePart.getSubmittedFileName();
//            String extension = "";
//            int i = fileName.lastIndexOf('.');
//            if (i > 0) {
//                extension = fileName.substring(i);
//            }
//            tempFile = File.createTempFile("upload", extension);
//            Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            throw e;
//        }
//
//        // step3: Gửi ảnh lên cloudinary
//        Map uploadResult = null;
//        try {
//            // Upload từ file tạm thời lên Cloudinary
//            uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());
//        } catch (IOException e) {
//            System.err.println("Error uploading to Cloudinary: " + e.getMessage());
//            throw e;
//        } finally {
//            // Đảm bảo file tạm thời được xóa
//            if (tempFile != null && tempFile.exists()) {
//                tempFile.delete();
//            }
//        }
//
//        // step 4: Lấy URL ảnh đã up
//        return (String) uploadResult.get("secure_url");
//    }
//}
