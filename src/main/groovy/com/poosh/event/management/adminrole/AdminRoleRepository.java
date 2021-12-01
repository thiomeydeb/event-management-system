package com.poosh.event.management.adminrole;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRoleRepository extends CrudRepository<AdminRole, Long> {
    @Modifying
    @Query("UPDATE AdminRole role SET role.isActive = ?2 WHERE role.id = ?1")
    void updateAdminRoleStatus(Long id, boolean status);
}
