package com.poosh.event.management.eventype;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTypeRepository extends CrudRepository<EventType, Long> {

    /*@Modifying
    @Query("UPDATE EventType et SET et.isActive = ?2 WHERE et.id = ?1")*/
    void updateEventPlannerAllocationStatus(Long id, boolean status);


}
