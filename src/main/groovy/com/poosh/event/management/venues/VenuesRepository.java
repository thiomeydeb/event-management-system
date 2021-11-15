package com.poosh.event.management.venues;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenuesRepository extends CrudRepository <Venues, Long> {
    @Modifying
    @Query("UPDATE Venues venues SET venues.isActive = ?2 WHERE venues.id = ?1")
    void updateVenueStatus(Long id, boolean status);
}
