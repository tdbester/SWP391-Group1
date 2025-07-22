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
import static org.example.talentcenter.utilities.Const.TYPE_COURSE;

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
        // check user login. If user don't login -> redirect to login page
        HttpSession session = request.getSession(false);
        String action = request.getParameter("action");
        if (action == null) action = "list";

        if (session == null || session.getAttribute("accountId") == null) {
            if(!action.equals("view")){
                response.sendRedirect("login");
                return;
            }
        }
        String role = (String) session.getAttribute("userRole");


        switch (action) {
            case "new":
                if(role!= null &&!role.equalsIgnoreCase("sale"))
                    break;
                showNewBlogForm(request, response);
                break;
            case "edit":
                if(role!= null &&!role.equalsIgnoreCase("sale"))
                    break;
                showEditBlogForm(request, response);
                break;
            case "delete":
                if(role!= null &&!role.equalsIgnoreCase("sale"))
                    break;
                deleteBlog(request, response);
                break;
            case "view":
                showBlogDetail(request, response,role!= null && role.equalsIgnoreCase("sale"));
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
        // search value
        String search = request.getParameter("search");
        String catParam  = request.getParameter("category");

        // number of page
        int index = 1;
        if (request.getParameter("index") != null) {
            index = Integer.parseInt(request.getParameter("index"));
        }
        Category filterCat;

        if (catParam != null && !catParam.isBlank()) {
            filterCat = categoryDAO.getById(Integer.parseInt(catParam));
        } else {
            filterCat= null;
        }

        // lấy danh sách blog từ database lên theo phân trang
        List<BlogDto> blogs = blogDAO.pagingBlog(index);
        List<BlogDto> filtered = blogs.stream()
                .filter(blog -> search == null ||
                        blog.getTitle().toLowerCase().contains(search.toLowerCase()))
                .toList();
        var categoryFiltered = filterCat != null
                ? filtered.stream()
                .filter(c -> c.getCategory() == filterCat.getId())
                .toList()
                : filtered;

        // Lấy danh sách category để hiển thị tên
        List<Category> categories = categoryDAO.getByType(TYPE_BLOG);
        request.setAttribute("categories", categories);

        request.setAttribute("blogList", categoryFiltered);
        int count = blogDAO.getTotalBlog();
        int endPage = count % 10 == 0 ? count / 10 : count / 10 + 1;
        request.setAttribute("endP", endPage);
        request.setAttribute("categories",  categoryDAO.getByType(TYPE_BLOG));
        request.setAttribute("selectedCategory", filterCat != null ? filterCat.getId() : null);
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

    private void showBlogDetail(HttpServletRequest request, HttpServletResponse response, boolean isSale)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Blog blog = blogDAO.getById(id);
        if(blog.getStatus() == 0 && !isSale){
            return;
        }
        request.setAttribute("blog", blog);
        request.getRequestDispatcher("/View/user-blog-detail.jsp").forward(request, response);
    }

    private void insertBlog(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        //1. lấy thông tin user đang đăng nhaapj để lưu vào AuthorId
        HttpSession session = request.getSession(false);
        int userId= (int) session.getAttribute("accountId");

        //2. lấy thông tin từ form JSP
        String title       = request.getParameter("title");
        String description = request.getParameter("description");
        String content     = request.getParameter("content");
        String statusParam = request.getParameter("status");
        int categoryId     = Integer.parseInt(request.getParameter("category"));
        Part imagePart     = request.getPart("imageFile");
        String imageUrl;

        //3. lấy ảnh và upload ln cloudinary, lấy về URL ảnh để lưu vào database
        if (imagePart != null && imagePart.getSize() > 0) {
            imageUrl = uploadToCloudinary(imagePart);
        } else {
            imageUrl = "https://placehold.co/600x400?text=img";
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

        //4. khởi tạo đối tượng blog theo các giá trị đã get ra ở treen
        Blog newBlog = new Blog();
        newBlog.setTitle(title);
        newBlog.setDescription(description);
        newBlog.setContent(content);
        newBlog.setImage(imageUrl);
        newBlog.setAuthorId(userId);
        newBlog.setCategory(categoryId);
        newBlog.setCreatedAt(new Date());
        newBlog.setStatus(status);

        //5. Dùng DAO để insert blog vào database
        blogDAO.insert(newBlog);
        response.sendRedirect("blogs");
    }

    private void updateBlog(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        //1. lấy thông tin người đang đăng nhập
        HttpSession session = request.getSession(false);
        int userId= (int) session.getAttribute("accountId");

        //2. lấy thông tin người dung nhập từ form
        int id          = Integer.parseInt(request.getParameter("id"));
        String title    = request.getParameter("title");
        String description = request.getParameter("description");
        String content  = request.getParameter("content");
        String statusParam = request.getParameter("status");
        int categoryId  = Integer.parseInt(request.getParameter("category"));
        Part imagePart  = request.getPart("imageFile");
        String imageUrl = request.getParameter("currentImageUrl");

        //3. upload ảnh lên cloudinary
        if (imagePart != null && imagePart.getSize() > 0) {
            imageUrl = uploadToCloudinary(imagePart);
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

        //4. Khởi tạo đối tượng blog từ casc giá trị lấy được ở trên
        Blog blog = new Blog();
        blog.setId(id);
        blog.setTitle(title);
        blog.setDescription(description);
        blog.setContent(content);
        blog.setImage(imageUrl);
        blog.setAuthorId(userId);
        blog.setCategory(categoryId);
        blog.setCreatedAt(new Date());
        blog.setStatus(status);

        //5. Gọi đến DAO để update blog
        blogDAO.update(blog);

        //6. Redirect sang trang list.
        response.sendRedirect("blogs");
    }

    private void deleteBlog(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // lấy Id blog cần delete
        int id = Integer.parseInt(request.getParameter("id"));

        // gọi đến DAO để delete
        blogDAO.delete(id);

        // chuển đến trang list
        response.sendRedirect("blogs");
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
