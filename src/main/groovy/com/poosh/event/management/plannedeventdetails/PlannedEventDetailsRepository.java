package com.poosh.event.management.plannedeventdetails;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedEventDetailsRepository extends CrudRepository<PlannedEventDetails, Long> {
    @Modifying
    @Query("UPDATE PlannedEventDetails details SET details.status = ?2 WHERE details.id = ?1")
    void updatePlannedEvenDetailStatus(Long id, boolean status);

}
