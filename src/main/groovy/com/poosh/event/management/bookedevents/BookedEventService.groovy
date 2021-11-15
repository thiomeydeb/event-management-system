package com.poosh.event.management.bookedevents

import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookedEventService {
    private final BookedEventRepository bookedEventRepository

    @Autowired //inject the object dependency implicitly
    public BookedEventService(BookedEventRepository bookedEventRepository) {
        this.bookedEventRepository = bookedEventRepository
    }

    void addBookedEvent (BookedEvent bookedEvent){
        bookedEventRepository.save(bookedEvent)
    }
    void updateBookedEvent(BookedEvent bookedEvent){
        bookedEventRepository.save(bookedEvent)
    }
    void updateStatus(Long id, int status){
        bookedEventRepository.updateBookedEventStatus(id, status)
    }

    void getAllBookedEvents(){
        bookedEventRepository.findAll()
    }

    def Object getBookedEventsById(long id) {
        def BookedEvent = bookedEventRepository.findById(id)
        BookedEvent;
    }
}
