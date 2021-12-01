package com.poosh.event.management.userpasswordhistory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPasswordHistoryRepository extends CrudRepository<UserPasswordHistory, Long> {
    Optional<UserPasswordHistory> findByUserId(Long userId);
}
