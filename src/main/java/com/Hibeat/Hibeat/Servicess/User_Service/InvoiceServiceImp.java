package com.Hibeat.Hibeat.Servicess.User_Service;

import com.Hibeat.Hibeat.Model.Admin.Address;
import com.Hibeat.Hibeat.Model.User.OrderProducts;
import com.Hibeat.Hibeat.Model.User.Orders;
import com.Hibeat.Hibeat.Repository.Admin.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class InvoiceServiceImp implements InvoiceService {

    private final OrderService orderService;
    private final AddressRepository addressRepository;

    @Autowired
    public InvoiceServiceImp(OrderService orderService, AddressRepository addressRepository) {
        this.orderService = orderService;
        this.addressRepository = addressRepository;
    }

    public byte[] createInvoice(Integer orderId) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);


            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                Orders orders = orderService.findByOrdersId(orderId);
                Address address = orders.getUser().getAddresses().get(orders.getAddressIndex());
                List<OrderProducts> products = orders.getOrderProducts();


                // Brand Name
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(250, 700);
                contentStream.showText("Hibeat");
                contentStream.endText();

                // Billing Address
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.showText("Shipping Address...");
                contentStream.newLineAtOffset(50, 670);

                contentStream.showText("House Name: " + address.getAddress());
                contentStream.newLineAtOffset(0, -20); // Move to the next line

                contentStream.showText("City: " + address.getCity());
                contentStream.newLineAtOffset(0, -20); // Adjust spacing

                contentStream.showText("Locality: " + address.getLocality());
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Pin: " + address.getPin());
                contentStream.newLineAtOffset(0, -20);

                contentStream.endText();

                // Add table headers
                float yPosition = 500;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Product Name");
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Product Price");
                contentStream.endText();

                // Move to the next line for the data
                yPosition -= 20;

                // Add table data
                for (OrderProducts product : products) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText(product.getProduct().getName());
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(String.valueOf(product.getQuantity()));
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(String.format("$%.2f", product.getProduct().getPrice()));
                    contentStream.endText();


                    // Move to the next line for the next product
                    yPosition -= 20;
                }

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Total Amount:");
                contentStream.newLineAtOffset(150, 0);
                contentStream.newLineAtOffset(150, 0);
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText(String.format("$%.2f", orders.getTotalAmount()));
                contentStream.endText();

                contentStream.close();
                document.save(baos);
                return baos.toByteArray();
            }

        }
    }
}