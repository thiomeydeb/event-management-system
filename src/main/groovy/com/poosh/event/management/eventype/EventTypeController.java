package com.poosh.event.management.eventype;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import com.poosh.event.management.eventype.dto.EventTypeCreateDto;
import com.poosh.event.management.eventype.dto.EventTypeUpdateDto;
import com.poosh.event.management.globaldto.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController()
@RequestMapping("api/v1/event-type")
public class EventTypeController {

    private final EventTypeService eventTypeService;

    @Autowired
    public EventTypeController(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    @GetMapping
    public BaseApiResponse getAllEventType(){
        return eventTypeService.getAllEventTypes();
    }

    @GetMapping("{id}")
    public BaseApiResponse getEventTypeById(@PathVariable("id") Long id){
        return eventTypeService.getEventTypeById(id);
    }

    @PostMapping
    public BaseApiResponse addEventType(@Valid @RequestBody EventTypeCreateDto body){
        return eventTypeService.addEventType(body);
    }

    @PutMapping("{id}")
    public BaseApiResponse updateEventType (@Valid @RequestBody EventTypeUpdateDto body,
                                            @PathVariable("id") @Min(1) Long eventId){
        return eventTypeService.updateEventType(eventId, body) ;
    }

    @PutMapping("status/{id}")
    public BaseApiResponse updateEventTypeStatus(@Valid @RequestBody StatusDto body,
                                                 @PathVariable("id") @Min(1) Long eventId){
        return eventTypeService.updateEventTypeStatus(eventId, body.getStatus());
    }

}

//Rest Interface UI -> Controller
//Business Logic Layer -> Service
//Data access Layer -> Repository