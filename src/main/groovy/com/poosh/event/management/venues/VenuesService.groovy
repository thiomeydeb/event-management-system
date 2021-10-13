package com.poosh.event.management.venues

import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class VenuesService {

    private final VenuesRepository venuesRepository

    @Autowired //inject the object dependency implicitly
    public VenuesService(VenuesRepository venuesRepository) {
        this.venuesRepository = venuesRepository
    }

    void addVenues (Venues venues){
        venuesRepository.save(venues)
    }

    void updateVenues(Venues venues){
        venuesRepository.save(venues)
    }

    void updateStatus(Long id, boolean status){
        venuesRepository.updateVenueStatus(id, status)
    }

    void getAllVenues(){
        venuesRepository.findAll()
    }


    def Object getVenuesById(long id) {
        def venue = venuesRepository.findById(id)
        venue;
    }
}
