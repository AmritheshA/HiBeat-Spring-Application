package com.Hibeat.Hibeat.Servicess.Admin_Service;

import java.io.IOException;

public interface SalesReportService {
    byte[] salesReport(String startDate, String endDate) throws IOException;

    byte[] monthlySalesReport() throws IOException;

    byte[] yearlySalesReport() throws IOException;

}
