package com.poosh.event.management.eventplannerallocations;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventPlannerAllocationRepository extends CrudRepository<EventPlannerAllocation, Long> {
    @Modifying
    @Query("UPDATE EventPlannerAllocation allocation SET allocation.status = ?2 WHERE allocation.id = ?1")
    void updateEventStatus(Long id, boolean status);


}
