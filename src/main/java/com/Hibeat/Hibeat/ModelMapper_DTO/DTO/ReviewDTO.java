package com.Hibeat.Hibeat.ModelMapper_DTO.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Data
public class ReviewDTO {

    private String reviewTitle;
    private String review;
    private MultipartFile image;
    private ArrayList<String> replays;
    private double rating;
    private int productId;

}
