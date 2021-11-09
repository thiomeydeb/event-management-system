package com.poosh.event.management.eventype;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventTypeRepository extends CrudRepository<EventType, Long> {
    @Modifying
    @Query("UPDATE EventType et SET et.isActive = ?2 WHERE et.id = ?1")
    void updateEventTypeStatus(Long id, boolean status);

    List<EventType> findByNameContaining(String value);

    @Query("SELECT et FROM EventType et WHERE et.isActive = ?1")
    Optional<EventType> selectAllEventTypes(boolean status);
}
