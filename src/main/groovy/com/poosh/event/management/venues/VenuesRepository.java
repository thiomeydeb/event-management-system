package com.poosh.event.management.venues;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenuesRepository extends CrudRepository <Venues, Long> {
    void updateVenueStatus(Long id, boolean status);
}
