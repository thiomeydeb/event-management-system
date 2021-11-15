package com.poosh.event.management.adminusers;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends CrudRepository<AdminUser, Long> {
    @Modifying
    @Query("UPDATE AdminUser user SET user.isActive = ?2 WHERE user.id = ?1")
    void updateAdminUserStatus(Long id, boolean status);

}
