package com.poosh.event.management.adminrolepermissions

import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AdminRolePermissionsService {

    private final AdminRolePermissionsRepository adminRolePermissionsRepository

    @Autowired //inject the object dependency implicitly
    public AdminRolePermissionsService(AdminRolePermissionsRepository adminRolePermissionsRepository) {
        this.adminRolePermissionsRepository = adminRolePermissionsRepository
    }

    void addAdminRolePermissions (AdminRolePermissionsRepository adminRolePermissionsRepository){
        adminRolePermissionsRepository.save(adminRolePermissionsRepository)
    }

    void updateAdminRolePermissions(AdminRolePermissionsRepository adminRolePermissionsRepository){
        adminRolePermissionsRepository.save(adminRolePermissionsRepository)
    }

    void updateStatus(Long id, boolean status){
        adminRolePermissionsRepository.updateAdminRolePermissionsStatus(id, status)
    }

    void getAllAdminRolePermissions(){
        adminRolePermissionsRepository.findAll()
    }

    def Object getAdminRolePermissionsById(long id) {
        def AdminRolePermissions = adminRolePermissionsRepository.findById(id)
        AdminRolePermissions;
    }
}
