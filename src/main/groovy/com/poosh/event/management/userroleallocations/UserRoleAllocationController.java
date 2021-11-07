package com.poosh.event.management.userroleallocations;

import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/role-allocation")
public class UserRoleAllocationController {

    private final UserRoleAllocationService userRoleAllocationService;

    @Autowired
    public UserRoleAllocationController(UserRoleAllocationService userRoleAllocationService) {
        this.userRoleAllocationService = userRoleAllocationService;
    }

    @GetMapping
    public void getAllRoleAllocationService(){
        userRoleAllocationService.getAllUserRoleAllocationService();
    }


    @GetMapping("/{id}")
    public Object getRoleAllocationById(@PathVariable("id") Long id){
        return userRoleAllocationService.getUserRoleAllocationById(id);
    }

    @PostMapping
    public void addUserRoleAllocationService(@RequestBody UserRoleAllocation body){
        userRoleAllocationService.addUserRoleAllocationService(body);
    }

}
