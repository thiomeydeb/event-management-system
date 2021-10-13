package com.poosh.event.management.userroleallocations

import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserRoleAllocationService {

    private final UserRoleAllocationRepository userRoleAllocationRepository

    @Autowired //inject the object dependency implicitly
    public UserRoleAllocationService(UserRoleAllocationRepository userRoleAllocationRepository) {
        this.userRoleAllocationRepository = userRoleAllocationRepository
    }

    void addUserRoleAllocationService (UserRoleAllocation userRoleAllocation){
        userRoleAllocationRepository.save(userRoleAllocation)
    }

    void updateUserRoleAllocation(UserRoleAllocation userRoleAllocation){
        userRoleAllocationRepository.save(userRoleAllocation)
    }

    void updateStatus(Long id, boolean status){
        userRoleAllocationRepository.updateUserRoleAllocationStatus(id, status)
    }

    void getAllUserRoleAllocationService(){
        userRoleAllocationRepository.findAll()
    }

    def getUserRoleAllocationById(Long id){
        def UserRoleAllocation = userRoleAllocationRepository.findById(id)
        UserRoleAllocation;
    }
}
