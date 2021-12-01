package com.poosh.event.management.dashboard;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping(value = "admin/collections")
    public BaseApiResponse getAdminDashboardData(){
        return dashboardService.getAdminDashboardData();
    }

    @GetMapping(value = "admin/collections/eventtype")
    public BaseApiResponse getCollectionsPerEventType(){
        return dashboardService.totalCollectionsPerEventType();
    }

    @GetMapping(value = "admin/eventcount/eventtype")
    public BaseApiResponse getEventCountPerType(){
        return dashboardService.totalEventCountPerEventType();
    }

    @GetMapping(value="client")
    public BaseApiResponse getClientDashboardData(Principal principal){
        return dashboardService.getClientDashboardData(principal);
    }
}
