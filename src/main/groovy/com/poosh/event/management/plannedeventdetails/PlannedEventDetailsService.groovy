package com.poosh.event.management.plannedeventdetails

import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PlannedEventDetailsService {

    private final PlannedEventDetailsRepository plannedEventDetailsRepository

    @Autowired //inject the object dependency implicitly
    public PlannedEventDetailsService(PlannedEventDetailsRepository plannedEventDetailsRepository) {
        this.plannedEventDetailsRepository = plannedEventDetailsRepository
    }

    void addPlannedEventDetail (PlannedEventDetails plannedEventDetails){
        plannedEventDetailsRepository.save(plannedEventDetails)
    }

    void updatePlannedEventDetails(PlannedEventDetails plannedEventDetails){
        plannedEventDetailsRepository.save(plannedEventDetails)
    }

    void updateStatus(Long id, boolean status){
        plannedEventDetailsRepository.updatePlannedEvenDetailStatus(id, status)
    }

    void getAllPlannedEventDetails(){
        plannedEventDetailsRepository.findAll()
    }

    def getPlannedEventDetailsById(Long id){
        def PlannedEventDetail = plannedEventDetailsRepository.findById(id)
        PlannedEventDetail;
    }
}
