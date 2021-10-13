package com.poosh.event.management.plannedeventdetails;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedEventDetailsRepository extends CrudRepository<PlannedEventDetails, Long> {
    void updatePlannedEvenDetailStatus(Long id, boolean status);

}
