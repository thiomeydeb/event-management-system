package com.poosh.event.management.eventype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/v1/event-type")
public class EventTypeController {

    private final EventTypeService eventTypeService;

    @Autowired
    public EventTypeController(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    @GetMapping
    public void getAllEvents(){
        eventTypeService.getAllEvents();
    }


    @GetMapping("/{id}")
    public Object getEventById(@PathVariable("id") Long id){
        return eventTypeService.getEventById(id);
    }

    @PostMapping
    public void addEventType(@RequestBody EventType body){
        eventTypeService.addEventType(body);
    }

}

//Rest Interface UI -> Controller
//Business Logic Layer -> Service
//Data access Layer -> Repository