package com.poosh.event.management.eventmanagement;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/event-management")
public class EventManagementController {
    private final EventManagementService eventManagementService;

    @Autowired
    public EventManagementController(EventManagementService eventManagementService) {
        this.eventManagementService = eventManagementService;
    }

    @PostMapping(value = "/link")
    public BaseApiResponse linkPlanner(@RequestBody @Valid LinkPlannerDto body){
        return eventManagementService.linkPlanner(body);
    }

    @PostMapping(value = "/cancelevent")
    public BaseApiResponse cancelEvent(HttpServletRequest request, WebRequest webRequest, Principal principal){
        return eventManagementService.cancelEvent(webRequest, request, principal);
    }

    @GetMapping(value = "/greening/{eventId}")
    public BaseApiResponse getSavedGreeningDetails(@PathVariable long eventId){
        return eventManagementService.getSavedGreeningDetails(eventId);
    }


    @PutMapping(value = "/greening/update")
    public BaseApiResponse updateGreeningDetails(@RequestBody String body,
                                                 HttpServletRequest request,
                                                 WebRequest webRequest,
                                                 Principal principal){
        return eventManagementService.saveGreeningDetails(body, request, webRequest, principal);
    }


}
