package com.web.rest;

import com.service.DashboardService;
import com.service.dto.CategoryDetailsDashboardDTO;
import com.service.dto.LocationDashboardDTO;
import com.service.dto.LocationFormDTO;
import com.service.dto.ValueDashboardByCategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardResource {
    private final DashboardService dashboardService;
    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<CategoryDetailsDashboardDTO>> byCategory(HttpServletRequest request){
        log.info("Get dashboard by category");
        return ResponseEntity.ok().body(dashboardService.valueDashboardByCategory(request));
    }

    @GetMapping("/by-location")
    public List<LocationDashboardDTO> byLocation(HttpServletRequest request){

        return dashboardService.locationDashboard(request);
    }
}
