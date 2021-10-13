package com.poosh.event.management.adminrolepermissions;

import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminRolePermissionsController {

    private final AdminRolePermissionsService adminRolePermissionsService;


    @Autowired
    public AdminRolePermissionsController(AdminRolePermissionsService adminRolePermissionsService) {
        this.adminRolePermissionsService = adminRolePermissionsService;
    }

    @GetMapping
    public void getAllAdminRolePermissions(){
        adminRolePermissionsService.getAllAdminRolePermissions();
    }

    @GetMapping("/{id}")
    public Object getAllAdminRolePermissionsById(@PathVariable("id") Long id){
        return adminRolePermissionsService.getAdminRolePermissionsById(id);
    }

//    @PostMapping
//    public void addAdminRolePermissions(@RequestBody AdminRolePermissions body){
//        adminRolePermissionsService.addAdminRolePermissions(body);
//    }
}
