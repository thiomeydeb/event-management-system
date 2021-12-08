package com.poosh.event.management.bookevent;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookEventRepository extends CrudRepository<BookEvent, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE BookedEvent event SET event.status = ?2 WHERE event.id = ?1")
    int updateBookedEventStatus(long id, int status);

}
