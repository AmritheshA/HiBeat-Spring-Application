package com.Hibeat.Hibeat.ModelMapper_DTO.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BannerDTO {

    private String title;
    private MultipartFile image;
    private String status;

}
