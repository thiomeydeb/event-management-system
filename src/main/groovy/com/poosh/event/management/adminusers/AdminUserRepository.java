package com.poosh.event.management.adminusers;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends CrudRepository<AdminUser, Long> {
    void updateAdminUserStatus(Long id, boolean status);

}
