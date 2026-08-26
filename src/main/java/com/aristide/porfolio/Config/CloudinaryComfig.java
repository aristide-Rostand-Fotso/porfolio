package com.aristide.porfolio.Config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryComfig {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    
    @Value("${cloudinary.api-key}")
    private String apiKey;

    
    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(Map.of("cloud_name", cloudName,
            "api_key", apiKey, "api_secret",apiSecret, "secure", true
        ));
    }

}
