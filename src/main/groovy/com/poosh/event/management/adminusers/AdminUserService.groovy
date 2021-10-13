package com.poosh.event.management.adminusers

import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AdminUserService {

    private final AdminUserRepository adminUserRepository

    @Autowired //inject the object dependency implicitly
    public AdminUserService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository
    }

    void addAdminUser (AdminUser adminUser){
        adminUserRepository.save(adminUser)
    }

    void updateAdminUser(AdminUser adminUser){
        adminUserRepository.save(adminUser)
    }

    void updateStatus(Long id, boolean status){
        adminUserRepository.updateAdminUserStatus(id, status)
    }

    void getAllAdminUsers(){
        adminUserRepository.findAll()
    }

    def Object getAdminUserById(long id) {
        def AdminUser = adminUserRepository.findById(id)
        AdminUser;
    }
}
