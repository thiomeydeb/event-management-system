package com.poosh.event.management.plannedeventdetails;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import com.poosh.event.management.globaldto.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/planned-event-details")
@Validated
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
    public BaseApiResponse updateProvidersDetails(@RequestBody String body,
                                                 HttpServletRequest request,
                                                 WebRequest webRequest,
                                                 Principal principal){
        return plannedEventDetailsService.updateProviderDetails(body, request, webRequest, principal);
    }

    @PutMapping(value = "/provider/update/{id}")
    public BaseApiResponse updateProviderDetails(@RequestBody @Valid StatusDto body,
                                                 @PathVariable("id") @Min(1L) Long plannedDetailsId,
                                                 HttpServletRequest request,
                                                 WebRequest webRequest,
                                                 Principal principal){
        return plannedEventDetailsService.updatePlannedDetailStatus(plannedDetailsId, body, request, webRequest, principal);
    }

}
