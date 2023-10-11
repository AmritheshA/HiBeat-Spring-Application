package com.Hibeat.Hibeat.ModelMapper_DTO.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class Product_DTO {

    private String productName;
    private String productDescription;
    private MultipartFile image;
    private String price;
    private int stock;
    private int categories;
    private String color;
    private String type;

}
