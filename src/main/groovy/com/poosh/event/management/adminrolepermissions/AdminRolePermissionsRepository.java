package com.poosh.event.management.adminrolepermissions;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRolePermissionsRepository extends CrudRepository<AdminRolePermissions, Long> {
    @Modifying
    @Query("UPDATE AdminRolePermissions permissions SET permissions.isActive = ?2 WHERE permissions.id = ?1")
    void updateAdminRolePermissionsStatus(Long id, boolean status);

}
