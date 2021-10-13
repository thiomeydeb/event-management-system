package com.poosh.event.management.adminroles;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRoleRepository extends CrudRepository<AdminRole, Long> {
    void updateAdminRoleStatus(Long id, boolean status);
}
