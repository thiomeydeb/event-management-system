package com.poosh.event.management.bookevent;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import com.poosh.event.management.bookevent.dto.EventCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/event")
@Validated
public class BookEventController {
    private final BookEventService bookEventService;

    @Autowired
    public BookEventController(BookEventService bookEventService) {
        this.bookEventService = bookEventService;
    }

    @PostMapping(value = "/book")
    public BaseApiResponse bookEvent(@RequestBody @Valid EventCreateDto bookedEventDetails, HttpServletRequest request, Principal principal){
        return bookEventService.bookEvent(bookedEventDetails, request, principal);
    }

    @GetMapping(value = "/{id}")
    public BaseApiResponse getBookedEvents(WebRequest request, @PathVariable Long id){
        return bookEventService.getBookedEventsByClient(id, request.getParameterMap());
    }

    @GetMapping(value = "/")
    public BaseApiResponse getAllBookedEvents(WebRequest request){
        return bookEventService.getAllBookedEvents(request.getParameterMap());
    }

}
