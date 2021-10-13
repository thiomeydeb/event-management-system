package com.poosh.event.management.bookedevents;

import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookedEventController {
    private final BookedEventService bookedEventService;

    @Autowired
    public BookedEventController(BookedEventService bookedEventService) {
        this.bookedEventService = bookedEventService;
    }

    @GetMapping
    public void getAllBookedEvents(){
        bookedEventService.getAllBookedEvents();
    }

    @GetMapping("/{id}")
    public Object getBookedEventsById(@PathVariable("id") Long id){
        return bookedEventService.getBookedEventsById(id);
    }

    @PostMapping
    public void addBookedEvents(@RequestBody BookedEvent body){
        bookedEventService.addBookedEvent(body);
    }
}
