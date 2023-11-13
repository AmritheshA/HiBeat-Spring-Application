package com.Hibeat.Hibeat.ModelMapper_DTO.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order_DTO {

    private int addressIndex;
    private String paymentMethod;
    private String paymentId;

}
