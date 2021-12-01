package com.poosh.event.management.adminusers;

import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin-user")
public class AdminUserController {
    private final AdminUserService adminUserService;

    @Autowired
    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public void getAllAdminUsers(){
        adminUserService.getAllAdminUsers();
    }

    @GetMapping("/{id}")
    public Object getAdminUserById(@PathVariable("id") Long id){
        return adminUserService.getAdminUserById(id);
    }

    @PostMapping
    public void addAdminUser(@RequestBody AdminUser body){
        adminUserService.addAdminUser(body);
    }

}
