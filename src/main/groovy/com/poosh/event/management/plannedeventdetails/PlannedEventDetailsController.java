package com.poosh.event.management.plannedeventdetails;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/planned-event-details")
public class PlannedEventDetailsController {

    private final PlannedEventDetailsService plannedEventDetailsService;

    @Autowired
    public PlannedEventDetailsController(PlannedEventDetailsService plannedEventDetailsService) {
        this.plannedEventDetailsService = plannedEventDetailsService;
    }

    @GetMapping(value = "/{eventId}")
    public BaseApiResponse getPlannedEventDetails(@PathVariable long eventId){
        return plannedEventDetailsService.getPlannedProviderDetails(eventId);
    }

    @GetMapping(value = "/assigned/events")
    public BaseApiResponse getBookedEvents(WebRequest request, Principal principal){
        return plannedEventDetailsService.getBookedEventsAssignedToPlanner(principal, request.getParameterMap());
    }

    @PutMapping(value = "/providers/update")
    public BaseApiResponse updateProviderDetails(@RequestBody String body,
                                                 HttpServletRequest request,
                                                 WebRequest webRequest,
                                                 Principal principal){
        return plannedEventDetailsService.updateProviderDetails(body, request, webRequest, principal);
    }

}
