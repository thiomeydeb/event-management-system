package com.poosh.event.management.venue;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import com.poosh.event.management.globaldto.StatusDto;
import com.poosh.event.management.venue.dto.VenueCreateDto;
import com.poosh.event.management.venue.dto.VenueUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("api/v1/venue")
@Validated
public class VenueController {

    private final VenueService venueService;

    @Autowired
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping
    public BaseApiResponse getAllVenues(){
        return venueService.getAllVenues();
    }

    @GetMapping("{id}")
    public BaseApiResponse getVenueById(@PathVariable("id") @Min(1) Long id){
        return venueService.getVenueById(id);
    }

    @PostMapping
    public BaseApiResponse addVenue(@Valid @RequestBody VenueCreateDto body){
        return venueService.addVenue(body);
    }

    @PutMapping("{id}")
    public BaseApiResponse updateVenue (@Valid @RequestBody VenueUpdateDto body,
                                            @PathVariable("id") @Min(1) Long venueId){
        return venueService.updateVenue(venueId, body);
    }

    @PutMapping("status/{id}")
    public BaseApiResponse updateVenueStatus(@Valid @RequestBody StatusDto body,
                                                 @PathVariable("id") @Min(1) Long venueId){
        return venueService.updateVenueStatus(venueId, body.getStatus());
    }
}
