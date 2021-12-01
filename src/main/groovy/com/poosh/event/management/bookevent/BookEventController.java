package com.poosh.event.management.bookevent;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/booked-event")
public class BookEventController {
    private final BookEventService bookEventService;

    @Autowired
    public BookEventController(BookEventService bookEventService) {
        this.bookEventService = bookEventService;
    }

    @PostMapping(value = "")
    public BaseApiResponse bookEvent(@RequestBody String bookedEventDetails, HttpServletRequest request, Principal principal){
        return bookEventService.bookEvent(bookedEventDetails, request, principal);
    }

    @GetMapping(value = "/{id}")
    public BaseApiResponse getBookedEvents(WebRequest request, @PathVariable Long id){
        return bookEventService.getBookedEventsByClient(id, request.getParameterMap());
    }

    @GetMapping(value = "/all")
    public BaseApiResponse getAllBookedEvents(WebRequest request){
        return bookEventService.getAllBookedEvents(request.getParameterMap());
    }

}
