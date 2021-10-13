package com.poosh.event.management.eventplannerallocations;

import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EventPlannerAllocationController {

    private final EventPlannerAllocationService eventPlannerAllocationService;

    @Autowired
    public EventPlannerAllocationController(EventPlannerAllocationService eventPlannerAllocationService) {
        this.eventPlannerAllocationService = eventPlannerAllocationService;
    }

    @GetMapping
    public void getAllEventPlannerAllocations(){
        eventPlannerAllocationService.getAllEventPlannerAllocations();
    }


    @GetMapping("/{id}")
    public Object getEventPlannerAllocationById(@PathVariable("id") Long id){
        return eventPlannerAllocationService.getEventPlannerAllocationById(id);
    }

//    @PostMapping
//    public void addEventPlannerAllocation(@RequestBody EventType body){
//        eventPlannerAllocationService.addEventPlannerAllocation(body);
//    }

}
