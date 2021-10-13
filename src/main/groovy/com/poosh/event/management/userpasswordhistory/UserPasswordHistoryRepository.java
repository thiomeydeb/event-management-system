package com.poosh.event.management.userpasswordhistory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordHistoryRepository extends CrudRepository<UserPasswordHistory, Long> {

    void updateUserPasswordHistoryStatus(Long id, boolean status);
}
