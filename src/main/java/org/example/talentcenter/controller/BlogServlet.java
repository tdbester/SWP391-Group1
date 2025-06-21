package org.example.talentcenter.controller;

import com.cloudinary.Singleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.talentcenter.dao.BlogDAO;
import org.example.talentcenter.dao.CategoryDAO;
import org.example.talentcenter.dto.BlogDto;
import org.example.talentcenter.model.Blog;
import org.example.talentcenter.model.Category;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import static org.example.talentcenter.utilities.Const.TYPE_BLOG;

@WebServlet("/blogs")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize       = 1024 * 1024 * 10, // 10MB
        maxRequestSize    = 1024 * 1024 * 50  // 50MB
)
public class BlogServlet extends HttpServlet {
    private BlogDAO blogDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        blogDAO = new BlogDAO();
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("user") == null) {
//            response.sendRedirect("View/login.jsp");
//            return;
//        }
        String action = request.getParameter("action");
        if (action == null) action = "list";

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
        if (action == null) action = "list";

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
        int index = 1;
        if (request.getParameter("index") != null) {
            index = Integer.parseInt(request.getParameter("index"));
        }

        List<BlogDto> blogs = blogDAO.pagingBlog(index);
        List<BlogDto> filtered = blogs.stream()
                .filter(blog -> search == null ||
                        blog.getTitle().toLowerCase().contains(search.toLowerCase()))
                .toList();

        // Lấy danh sách category để hiển thị tên
        List<Category> categories = categoryDAO.getByType(TYPE_BLOG);
        request.setAttribute("categories", categories);

        request.setAttribute("blogList", filtered);
        int count = blogDAO.getTotalBlog();
        int endPage = count % 10 == 0 ? count / 10 : count / 10 + 1;
        request.setAttribute("endP", endPage);
        request.getRequestDispatcher("/View/blog.jsp").forward(request, response);
    }

    private void showNewBlogForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Đưa danh sách category cho form
        List<Category> categories = categoryDAO.getByType(TYPE_BLOG);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/View/blog-form.jsp").forward(request, response);
    }

    private void showEditBlogForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Blog existingBlog = blogDAO.getById(id);

        // Đưa blog và danh sách category cho form
        request.setAttribute("blog", existingBlog);
        List<Category> categories = categoryDAO.getByType(TYPE_BLOG);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/View/blog-form.jsp").forward(request, response);
    }

    private void showBlogDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Blog blog = blogDAO.getById(id);
        request.setAttribute("blog", blog);
        request.getRequestDispatcher("/View/user-blog-detail.jsp").forward(request, response);
    }

    private void insertBlog(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        int userId= (int) session.getAttribute("accountId");

        String title       = request.getParameter("title");
        String description = request.getParameter("description");
        String content     = request.getParameter("content");
        int categoryId     = Integer.parseInt(request.getParameter("category"));
        Part imagePart     = request.getPart("imageFile");
        String imageUrl;

        if (imagePart != null && imagePart.getSize() > 0) {
            imageUrl = uploadToCloudinary(imagePart);
        } else {
            imageUrl = "https://res.cloudinary.com/your-cloud-name/image/upload/v1/default_image.png";
        }
        Blog newBlog = new Blog();
        newBlog.setTitle(title);
        newBlog.setDescription(description);
        newBlog.setContent(content);
        newBlog.setImage(imageUrl);
        newBlog.setAuthorId(userId);
        newBlog.setCategory(categoryId);
        newBlog.setCreatedAt(new Date());

        blogDAO.insert(newBlog);
        response.sendRedirect("blogs");
    }

    private void updateBlog(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        int userId= (int) session.getAttribute("accountId");
        int id          = Integer.parseInt(request.getParameter("id"));
        String title    = request.getParameter("title");
        String description = request.getParameter("description");
        String content  = request.getParameter("content");
        int categoryId  = Integer.parseInt(request.getParameter("category"));
        Part imagePart  = request.getPart("imageFile");
        String imageUrl = request.getParameter("currentImageUrl");

        if (imagePart != null && imagePart.getSize() > 0) {
            imageUrl = uploadToCloudinary(imagePart);
        }

        Blog blog = new Blog();
        blog.setId(id);
        blog.setTitle(title);
        blog.setDescription(description);
        blog.setContent(content);
        blog.setImage(imageUrl);
        blog.setAuthorId(userId);
        blog.setCategory(categoryId);
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

    private String uploadToCloudinary(Part filePart) throws IOException, ServletException {
        Cloudinary cloudinary = Singleton.getCloudinary();
        if (cloudinary == null) {
            throw new ServletException("Cloudinary not configured");
        }

        File tempFile;
        try (InputStream input = filePart.getInputStream()) {
            String fileName  = filePart.getSubmittedFileName();
            String extension = fileName.substring(fileName.lastIndexOf('.'));
            tempFile = File.createTempFile("upload", extension);
            Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        Map<?,?> uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());
        tempFile.delete();
        return (String) uploadResult.get("secure_url");
    }
}
