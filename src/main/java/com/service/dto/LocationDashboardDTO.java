package com.service.dto;

import com.domain.Location;

import java.util.HashMap;
import java.util.Map;

public class LocationDashboardDTO {
    private Long id;

    private String nameAr;

    private String nameEn;
    private Map<String, Integer> counts;

    public LocationDashboardDTO(Location location) {
        this.id = location.getId();
        this.nameAr = location.getNameAr();
        this.nameEn = location.getNameEn();
        this.counts = new HashMap<>();
    }

    public LocationDashboardDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Map<String, Integer> getCounts() {
        return counts;
    }

    public void setCounts(Map<String, Integer> counts) {
        this.counts = counts;
    }
}
