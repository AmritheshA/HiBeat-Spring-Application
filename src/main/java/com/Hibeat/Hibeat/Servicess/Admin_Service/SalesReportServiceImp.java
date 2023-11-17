package com.Hibeat.Hibeat.Servicess.Admin_Service;

import com.Hibeat.Hibeat.Model.User.Orders;
import com.Hibeat.Hibeat.Repository.User.OrderRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class SalesReportServiceImp implements SalesReportService {


    private final OrderRepository orderRepository;
    private final AdminDashboardServiceImp adminDashboardServiceImp;

    @Autowired
    public SalesReportServiceImp(OrderRepository orderRepository, AdminDashboardServiceImp adminDashboardServiceImp) {
        this.orderRepository = orderRepository;
        this.adminDashboardServiceImp = adminDashboardServiceImp;
    }

    @Override
    public byte[] salesReport(String stringStartDate, String stringEndDate) throws IOException {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);


            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                List<Orders> ordersList = generateMonthlyReport(stringStartDate, stringEndDate);

                // Brand Name
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(250, 700);
                contentStream.showText("Sales Report");
                contentStream.endText();


                // Add table headers
                float yPosition = 575;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Order Id");
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(50, 0);
                contentStream.showText("Order Date");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Payment Method");
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(50, 0);
                contentStream.showText("Order Total");
                contentStream.endText();

                // Move to the next line for the data
                yPosition -= 20;

                // Add table data
                for (Orders orders : ordersList) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText(orders.getOrderId());
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(50, 0);
                    contentStream.showText(String.valueOf(orders.getOrderDate()));
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(String.valueOf(orders.getPayments().getPaymentMethod()));
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(50, 0);
                    contentStream.showText(String.format("$%.2f", orders.getTotalAmount()));
                    contentStream.endText();

                    // Move to the next line for the next orders
                    yPosition -= 20;
                }
                contentStream.close();
                document.save(baos);
                return baos.toByteArray();
            }

        }
    }

    @Override
    public byte[] monthlySalesReport() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);


            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                Map<String, Double> salesReport = adminDashboardServiceImp.monthlySales();

                // Brand Name
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(250, 700);
                contentStream.showText("Sales Report");
                contentStream.endText();


                // Add table headers
                float yPosition = 575;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Month ");
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(50, 0);
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(50, 0);
                contentStream.showText("Order Total");
                contentStream.endText();

                // Move to the next line for the data
                yPosition -= 20;

                // Add table data
                for (String keys : salesReport.keySet()) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText(keys);
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(50, 0);
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(50, 0);
                    contentStream.showText(String.format("$%.2f",salesReport.get(keys)));
                    contentStream.endText();

                    // Move to the next line for the next orders
                    yPosition -= 20;
                }
                contentStream.close();
                document.save(baos);
                return baos.toByteArray();
            }

        }
    }

    @Override
    public byte[] yearlySalesReport() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PDDocument document = new PDDocument()) {


            PDPage page = new PDPage();
            document.addPage(page);


            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                Map<String, Double> salesReport = adminDashboardServiceImp.yearlySales();

                // Brand Name
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(250, 700);
                contentStream.showText("Sales Report");
                contentStream.endText();


                // Add table headers
                float yPosition = 575;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Year ");
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(50, 0);
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(100, 0);
                contentStream.newLineAtOffset(50, 0);
                contentStream.showText("Order Total");
                contentStream.endText();

                // Move to the next line for the data
                yPosition -= 20;

                // Add table data
                for (String keys : salesReport.keySet()) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText(keys);
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(50, 0);
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.newLineAtOffset(50, 0);
                    contentStream.showText(String.format("$%.2f", salesReport.get(keys)));
                    contentStream.endText();

                    // Move to the next line for the next orders
                    yPosition -= 20;
                }
                contentStream.close();
                document.save(baos);
                return baos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }


    public List<Orders> generateMonthlyReport(String stringStartDate, String stringEndDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parse the string to a LocalDate
        LocalDate startDate = LocalDate.parse(stringStartDate, formatter);
        LocalDate endDate = LocalDate.parse(stringEndDate, formatter);


        List<Orders> orders = orderRepository.monthlySalesReport(startDate, endDate);
        return orders;
    }


}
