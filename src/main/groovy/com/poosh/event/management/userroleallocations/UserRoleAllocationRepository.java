package com.poosh.event.management.userroleallocations;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleAllocationRepository extends CrudRepository<UserRoleAllocation, Long> {
    void updateUserRoleAllocationStatus(Long id, boolean status);
}
