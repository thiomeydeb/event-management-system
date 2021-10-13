package com.poosh.event.management.bookedevents;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookedEventRepository extends CrudRepository<BookedEvent, Long> {
    void updateBookedEventStatus(Long id, boolean status);

}
