package com.poosh.event.management.plannedeventdetails;

import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/planned-event-details")
public class PlannedEventDetailsController {

    private final PlannedEventDetailsService plannedEventDetailsService;

    @Autowired
    public PlannedEventDetailsController(PlannedEventDetailsService plannedEventDetailsService) {
        this.plannedEventDetailsService = plannedEventDetailsService;
    }

    @GetMapping
    public void getAllPlannedEventDetails(){
        plannedEventDetailsService.getAllPlannedEventDetails();
    }


    @GetMapping("/{id}")
    public Object getPlannedEventDetailsById(@PathVariable("id") Long id){
        return plannedEventDetailsService.getPlannedEventDetailsById(id);
    }

    @PostMapping
    public void addPlannedEventDetail(@RequestBody PlannedEventDetails body){
        plannedEventDetailsService.addPlannedEventDetail(body);
    }

}
