package com.poosh.event.management.eventplannerallocations;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventPlannerAllocationRepository extends CrudRepository<EventPlannerAllocation, Long> {
    void updateEventStatus(Long id, boolean status);


}
