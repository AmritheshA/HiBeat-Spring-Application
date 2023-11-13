package com.Hibeat.Hibeat.ModelMapper_DTO.DTO;

import lombok.Data;

@Data
public class ProductOfferDTO {

    private Integer productId;
    private Integer offerId;
    private double offerPercentage;
    private String expiryDate;
}
