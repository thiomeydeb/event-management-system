package com.poosh.event.management.bookedevents;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookedEventRepository extends CrudRepository<BookedEvent, Long> {
    @Modifying
    @Query("UPDATE BookedEvent event SET event.status = ?2 WHERE event.id = ?1")
    void updateBookedEventStatus(long id, int status);

}
