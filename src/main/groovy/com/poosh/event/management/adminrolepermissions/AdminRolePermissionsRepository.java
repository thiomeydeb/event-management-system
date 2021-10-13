package com.poosh.event.management.adminrolepermissions;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRolePermissionsRepository extends CrudRepository<AdminRolePermissions, Long> {
    void updateAdminRolePermissionsStatus(Long id, boolean status);

}
