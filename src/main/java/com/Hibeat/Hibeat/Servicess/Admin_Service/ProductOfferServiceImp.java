package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.Admin.ProductOffers;
import com.Hibeat.Hibeat.Model.Admin.Products;
import com.Hibeat.Hibeat.ModelMapper_DTO.DTO.ProductOfferDTO;
import com.Hibeat.Hibeat.Repository.Admin.ProductOfferRepository;
import com.Hibeat.Hibeat.Repository.Admin.ProductRepository;
import com.Hibeat.Hibeat.Servicess.User_Service.ProductService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@Slf4j
public class ProductOfferServiceImp implements ProductOfferService{

    private final ProductOfferRepository productOfferRepository;

    private final ProductService productService;

    @Autowired
    public ProductOfferServiceImp(ProductOfferRepository productOfferRepository, ProductService productService) {
        this.productOfferRepository = productOfferRepository;
        this.productService = productService;
    }


    @Override
    public String productOffers(Model model) {

        List<ProductOffers> offersList = productOfferRepository.findAll();
        model.addAttribute("productOffers",offersList);

        return "Admin/productOff";
    }

    @Override
    public String addProductOffer(ProductOfferDTO productOffer) {
        try {
            Products product = productService.findAllById(productOffer.getProductId());

            ProductOffers productOfferEntity = new ProductOffers();
            productOfferEntity.setProducts(product);
            productOfferEntity.setOfferPercentage(productOffer.getOfferPercentage());
            productOfferEntity.setExpiryDate(productOffer.getExpiryDate());

            double priceReduction = (productOfferEntity.getOfferPercentage() / 100.0) * product.getPrice();
            productOfferEntity.setDiscountAmount(priceReduction);

            saveOffer(productOfferEntity);

            product.setPrice(product.getPrice() - priceReduction);
            productService.save(product);

            return "redirect:/admin/product-off";
        } catch (Exception e) {
            log.error("addProductOffer " + e.getMessage(), e);
            return "Exception/404";
        }
    }


    @Override
    public ProductOffers saveOffer(ProductOffers productOffers) {
        return productOfferRepository.save(productOffers);
    }


    @Override
    public String editProductOff(ProductOfferDTO productOfferDTO) {

        try {
            ProductOffers productOffers = productOfferRepository.findById(productOfferDTO.getOfferId()).get();
            double price = (productOffers.getOfferPercentage() / 100) * productOffers.getProducts().getPrice();

            productOffers.setOfferPercentage(productOfferDTO.getOfferPercentage());
            productOffers.setDiscountAmount(price);
            productOffers.setExpiryDate(productOfferDTO.getExpiryDate());

            saveOffer(productOffers);
            return "redirect:/admin/product-off";
        }catch (Exception e){
            log.info("editProductOff "+e.getMessage());
            return "Exception/404";
        }
    }

    @Override
    @Transactional
    public String removeProductOffer(Integer offerId) {

        try {
            ProductOffers productOffers = findByOfferId(offerId);
            double productPrice = productOffers.getProducts().getPrice();
            double discountPrice = productOffers.getDiscountAmount();
            Products products = productOffers.getProducts();
            products.setPrice(productPrice + discountPrice);
            productService.save(products);

            productOfferRepository.deleteById(offerId);

            return "redirect:/admin/product-off";
        }catch (Exception c){

            return "Exception/404";
        }

    }

    @Override
    public ProductOffers findByOfferId(Integer offerId) {
        return productOfferRepository.findById(offerId).get();
    }
}
