package com.poosh.event.management.venue;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends CrudRepository <Venue, Long> {
    @Modifying
    @Query("UPDATE Venue venue SET venue.isActive = ?2 WHERE venue.id = ?1")
    void updateVenueStatus(Long id, boolean status);
}
