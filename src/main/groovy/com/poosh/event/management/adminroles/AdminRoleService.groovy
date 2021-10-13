package com.poosh.event.management.adminroles

import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AdminRoleService {

    private final AdminRoleRepository adminRoleRepository

    @Autowired //inject the object dependency implicitly
    public AdminRoleService(AdminRoleRepository adminRoleRepository) {
        this.adminRoleRepository = adminRoleRepository
    }

    void addAdminRole (AdminRole adminRole){
        adminRoleRepository.save(adminRole)
    }

    void updateAdminRole (AdminRole adminRole){
        adminRoleRepository.save(adminRole)
    }

    void updateStatus(Long id, boolean status){
        adminRoleRepository.updateAdminRoleStatus(id, status)
    }

    void getAllAdminRoles(){
        adminRoleRepository.findAll()
    }


    def Object getAllAdminRoleById(long id) {
        def AdminRole = adminRoleRepository.findById(id)
        AdminRole;
    }
}
