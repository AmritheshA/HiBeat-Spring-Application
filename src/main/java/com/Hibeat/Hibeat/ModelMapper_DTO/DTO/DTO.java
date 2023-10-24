package com.Hibeat.Hibeat.ModelMapper_DTO.DTO;

import lombok.Data;
import org.springframework.context.annotation.Bean;


@Data
public class DTO {

    private String name;
    private String email;
    private String password;
    private String token;
    private String role;
    private String mobile;

}
