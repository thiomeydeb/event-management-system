package com.poosh.event.management.venues;

import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/venues")
public class VenuesController {

    private final VenuesService venuesService;

    @Autowired
    public VenuesController(VenuesService venuesService) {
        this.venuesService = venuesService;
    }

    @GetMapping
    public void getAllVenues(){
        venuesService.getAllVenues();
    }

    @GetMapping("/{id}")
    public Object getVenuesById(@PathVariable("id") Long id){
        return venuesService.getVenuesById(id);
    }

    @PostMapping
    public void addVenues(@RequestBody Venues body){
        venuesService.addVenues(body);
    }
}
