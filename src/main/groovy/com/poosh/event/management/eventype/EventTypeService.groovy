package com.poosh.event.management.eventype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventTypeService {

    private final EventTypeRepository eventTypeRepository

    @Autowired //inject the object dependency implicitly
    public EventTypeService(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository
    }

   void addEventType (EventType eventType){
       eventTypeRepository.save(eventType)
   }

    void updateEventType(EventType eventType){
        eventTypeRepository.save(eventType)
    }

    void updateStatus(Long id, boolean status){
        eventTypeRepository.updateEventStatus(id, status)
    }

    void getAllEvents(){
        eventTypeRepository.findAll()
    }

    def getEventById(Long id){
        def event = eventTypeRepository.findById(id)
        event;
    }
}
