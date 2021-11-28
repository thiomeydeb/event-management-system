package com.poosh.event.management.adminrole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin-role")
public class AdminRoleController {

    private final AdminRoleService adminRoleService;

    @Autowired
    public AdminRoleController(AdminRoleService adminRoleService) {
        this.adminRoleService = adminRoleService;
    }

    @GetMapping
    public void getAllAdminRole(){
        adminRoleService.getAllAdminRoles();
    }

    @GetMapping("/{id}")
    public Object getAllAdminRoleById(@PathVariable("id") Long id){
        return adminRoleService.getAllAdminRoleById(id);
    }

    @PostMapping
    public void addAdminRole(@RequestBody AdminRole body){
        adminRoleService.addAdminRole(body);
    }



}
