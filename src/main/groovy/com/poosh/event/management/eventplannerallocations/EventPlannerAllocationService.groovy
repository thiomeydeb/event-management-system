package com.poosh.event.management.eventplannerallocations

import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EventPlannerAllocationService {

    private final EventPlannerAllocationRepository eventPlannerAllocationRepository

    @Autowired //inject the object dependency implicitly
    public EventPlannerAllocationService(EventPlannerAllocationRepository eventPlannerAllocationRepository) {
        this.eventPlannerAllocationRepository = eventPlannerAllocationRepository
    }

    void addEventPlannerAllocation (EventPlannerAllocation eventPlannerAllocation){
        eventPlannerAllocationRepository.save(eventPlannerAllocation)
    }

    void updateEventPlannerAllocation(EventPlannerAllocation eventPlannerAllocation){
        eventPlannerAllocationRepository.save(eventPlannerAllocation)
    }

    void updateStatus(Long id, boolean status){
        eventPlannerAllocationRepository.updateEventPlannerAllocationStatus(id, status)
    }

    void getAllEventPlannerAllocations(){
        eventPlannerAllocationRepository.findAll()
    }

    def Object getEventPlannerAllocationById(long id) {
        def EvenPlannerAllocation = eventPlannerAllocationRepository.findById(id)
        EvenPlannerAllocation;
    }
}
