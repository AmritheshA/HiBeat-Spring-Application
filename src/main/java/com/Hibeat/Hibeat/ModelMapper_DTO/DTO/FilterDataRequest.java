package com.Hibeat.Hibeat.ModelMapper_DTO.DTO;

import java.util.Map;
import java.util.List;

public class FilterDataRequest {
    private Map<String, List<String>> filterData;
    private String status;

    public Map<String, List<String>> getFilterData() {
        return filterData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFilterData(Map<String, List<String>> filterData) {
        this.filterData = filterData;
    }
}
