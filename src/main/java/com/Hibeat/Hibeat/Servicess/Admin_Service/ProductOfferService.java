package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.Admin.ProductOffers;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.ProductOfferDTO;
import org.springframework.ui.Model;

public interface ProductOfferService {
    String productOffers(Model model);

    String addProductOffer(ProductOfferDTO productOffers);

    ProductOffers saveOffer(ProductOffers productOffers);

    String editProductOff(ProductOfferDTO productOfferDTO);

    String removeProductOffer(Integer productId);

}
