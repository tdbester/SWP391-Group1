package org.example.talentcenter.controller.config;

import com.cloudinary.Cloudinary;
//import com.cloudinary.Singleton;
//import com.cloudinary.SingletonManager;
import com.cloudinary.utils.ObjectUtils;

//import com.cloudinary.Singleton;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.Map;

// giúp hệ thống thực hiện hành động lắng nghe sự kiện từ bên ngoài.
@WebListener
public class CloudinaryConfig implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("CloudinaryConfigListener initialized...");
        Map config = ObjectUtils.asMap(
                "cloud_name", "dp0pdvjyy",
                "api_key", "461366976878155",
                "api_secret", "UJPcbc1aJ7ilfH5D0yTtddy9Y28",
                "secure", true
        );
        Cloudinary cloudinary = new Cloudinary(config);
        //SingletonManager manager= new SingletonManager();
        //manager.setCloudinary(cloudinary);
        //manager.init();
        System.out.println("Cloudinary instance created and set to Singleton.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("CloudinaryConfigListener destroyed...");
    }
}
