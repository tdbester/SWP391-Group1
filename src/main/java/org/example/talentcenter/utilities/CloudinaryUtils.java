package org.example.talentcenter.utilities;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Map;

public class CloudinaryUtils {

    public static String uploadImage(Part filePart) throws IOException {
        try {
            Cloudinary cloudinary = Singleton.getCloudinary();

            // Upload file với transformation đúng cách
            Map uploadResult = cloudinary.uploader().upload(
                    filePart.getInputStream().readAllBytes(),
                    ObjectUtils.asMap(
                            "folder", "talent_center/avatars", // Tạo folder riêng
                            "width", 300,
                            "height", 300,
                            "crop", "fill",
                            "gravity", "face" // Tự động crop theo khuôn mặt
                    )
            );

            return (String) uploadResult.get("secure_url");

        } catch (Exception e) {
            throw new IOException("Lỗi upload ảnh: " + e.getMessage());
        }
    }

    public static void deleteImage(String imageUrl) {
        try {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Cloudinary cloudinary = Singleton.getCloudinary();

                // Extract public_id from URL
                String publicId = extractPublicId(imageUrl);
                if (publicId != null) {
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi xóa ảnh cũ: " + e.getMessage());
        }
    }

    private static String extractPublicId(String imageUrl) {
        try {
            // URL format: https://res.cloudinary.com/your-cloud/image/upload/v123456/folder/filename.jpg
            String[] parts = imageUrl.split("/");
            String filename = parts[parts.length - 1];
            String folder = parts[parts.length - 2];

            // Remove file extension
            String filenameWithoutExt = filename.substring(0, filename.lastIndexOf('.'));

            return folder + "/" + filenameWithoutExt;
        } catch (Exception e) {
            return null;
        }
    }
}