package com.Hibeat.Hibeat.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "duktwv58k",
                "api_key", "431383961127827",
                "api_secret", "LlTya8IJXZGWDF2zscvjREz-UA4"
        ));
    }
}
