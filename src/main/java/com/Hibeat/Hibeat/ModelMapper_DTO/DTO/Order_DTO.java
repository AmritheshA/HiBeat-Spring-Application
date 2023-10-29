package com.Hibeat.Hibeat.ModelMapper_DTO.DTO;

import com.Hibeat.Hibeat.Model.CartProduct;
import com.Hibeat.Hibeat.Model.Payments;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order_DTO {

    private int addressIndex;
    private String paymentMethod;
    private String paymentId;

}
