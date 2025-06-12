package org.example.talentcenter.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.talentcenter.dao.BlogDAO;
import org.example.talentcenter.dto.BlogDto;
import org.example.talentcenter.model.Blog;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Date;

import jakarta.servlet.annotation.MultipartConfig;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;


import java.util.Map;


@WebServlet("/blogs")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
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
                showNewBlogForm(request, response);
                break;
            case "edit":
                showEditBlogForm(request, response);
                break;
            case "delete":
                deleteBlog(request, response);
                break;
            case "view":
                showBlogDetail(request, response);
                break;
            default:
                listBlogs(request, response);
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
        System.out.println("Action: " + action); // Log action for debugging


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
        String search = request.getParameter("search");
        Integer index = 1;
        if (request.getParameter("index") != null) {
             index = Integer.parseInt(request.getParameter("index"));
        }
        List<BlogDto> blogs = blogDAO.pagingBlog(index);
        List<BlogDto> filtered = blogs.stream().filter(blog ->
                search == null || blog.getTitle().toLowerCase().contains(search.toLowerCase())).toList();
        request.setAttribute("blogList", filtered);
        BlogDAO blogDAO = new BlogDAO();
        int count = blogDAO.getTotalBlog();
        int endPage = count % 5 == 0 ? count / 5 : count / 5 + 1;
        request.setAttribute("endP", endPage);
        request.getRequestDispatcher("./View/blog.jsp").forward(request, response);
    }


    private void showNewBlogForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("./View/blog-form.jsp").forward(request, response);
    }

    private void showBlogDetail(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
//            try{
                System.out.println(request.getParameter("id"));
                int id = Integer.parseInt(request.getParameter("id"));
                Blog blog = blogDAO.getById(id);
                request.setAttribute("blog", blog);
                request.getRequestDispatcher("./View/user-blog-detail.jsp").forward(request, response);
//            } catch (Exception e){
//                response.sendRedirect("blogs");
//            }
        }


    private void showEditBlogForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Blog existingBlog = blogDAO.getById(id);
        request.setAttribute("blog", existingBlog);
        request.getRequestDispatcher("./View/blog-form.jsp").forward(request, response);
    }


    private void insertBlog(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String content = request.getParameter("content");
        Part imagePart = request.getPart("imageFile"); // Tên của input type="file" trong form


        String imageUrl = null; // Khởi tạo null để dễ kiểm tra


        if (imagePart != null && imagePart.getSize() > 0) {
            try {
                imageUrl = uploadToCloudinary(imagePart);
            } catch (Exception e) {
                // Xử lý lỗi upload ảnh
                System.err.println("Error uploading image to Cloudinary: " + e.getMessage());
                request.setAttribute("errorMessage", "Failed to upload image. Please try again.");
                // Chuyển hướng về form hoặc hiển thị lỗi
                showNewBlogForm(request, response);
                return;
            }
        } else {
            // Nếu không có file ảnh được upload, bạn có thể đặt một URL ảnh mặc định hoặc để trống
            System.out.println("No image file provided for insertion.");
            imageUrl = "https://res.cloudinary.com/your-cloud-name/image/upload/v1/default_image.png"; // Thay bằng ảnh mặc định của bạn nếu có
        }


        Integer authorId = Integer.parseInt(request.getParameter("authorId"));


        Blog newBlog = new Blog();
        newBlog.setTitle(title);
        newBlog.setDescription(description);
        newBlog.setContent(content);
        newBlog.setImage(imageUrl);
        newBlog.setAuthorId(authorId);
        newBlog.setCreatedAt(new Date());

        System.out.println("Inserting Blog: " + newBlog.getTitle() + ", Image URL: " + newBlog.getImage());
        blogDAO.insert(newBlog);
        response.sendRedirect("blogs");
    }


    private void updateBlog(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String content = request.getParameter("content");
        Part imagePart = request.getPart("imageFile"); // Đảm bảo tên này khớp với form (tôi giả định là "imageFile")


        String imageUrl = request.getParameter("currentImageUrl"); // Lấy URL ảnh hiện tại từ một hidden input trong form

        // Kiểm tra nếu có ảnh mới được upload
        if (imagePart != null && imagePart.getSize() > 0) {
            try {
                imageUrl = uploadToCloudinary(imagePart);
            } catch (Exception e) {
                System.err.println("Error uploading new image to Cloudinary: " + e.getMessage());
                request.setAttribute("errorMessage", "Failed to upload new image. Please try again.");
                showEditBlogForm(request, response); // Quay lại form chỉnh sửa với lỗi
                return;
            }
        } else {
            System.out.println("No new image file provided for update. Keeping existing image.");
        }


        Integer authorId = Integer.parseInt(request.getParameter("authorId"));

        Blog blog = new Blog();
        blog.setId(id);
        blog.setTitle(title);
        blog.setDescription(description);
        blog.setContent(content);
        blog.setImage(imageUrl);
        blog.setAuthorId(authorId);
        blog.setCreatedAt(new Date());


        System.out.println("Updating Blog: " + blog.getTitle() + ", Image URL: " + blog.getImage());
        blogDAO.update(blog);
        response.sendRedirect("blogs");
    }


    private void deleteBlog(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        blogDAO.delete(id);
        response.sendRedirect("blogs");
    }


    private String uploadToCloudinary(Part filePart) throws IOException, ServletException {
        // step 1: khởi tạo 1 đối tượng để có thể call đến cloudinary
        Cloudinary cloudinary = Singleton.getCloudinary();
        if (cloudinary == null) {
            throw new ServletException("Error");
        }

        // step 2: đọc file mà người dùng upload lên
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
            throw e;
        }

        // step3: Gửi ảnh lên cloudinary
        Map uploadResult = null;
        try {
            // Upload từ file tạm thời lên Cloudinary
            uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());
        } catch (IOException e) {
            System.err.println("Error uploading to Cloudinary: " + e.getMessage());
            throw e;
        } finally {
            // Đảm bảo file tạm thời được xóa
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }

        // step 4: Lấy URL ảnh đã up
        return (String) uploadResult.get("secure_url");
    }

}



