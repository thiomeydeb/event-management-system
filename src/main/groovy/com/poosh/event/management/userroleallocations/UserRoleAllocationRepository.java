package com.poosh.event.management.userroleallocations;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleAllocationRepository extends CrudRepository<UserRoleAllocation, Long> {
    @Modifying
    @Query("UPDATE UserRoleAllocation allocation SET allocation.isActive = ?2 WHERE allocation.id = ?1")
    void updateUserRoleAllocationStatus(Long id, boolean status);
}
