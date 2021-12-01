package com.poosh.event.management.adminrole;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/admin-role")
public class AdminRoleController {

    private final AdminRoleService adminRoleService;

    @Autowired
    public AdminRoleController(AdminRoleService adminRoleService) {
        this.adminRoleService = adminRoleService;
    }

    @GetMapping(value = "")
    public BaseApiResponse getRoles(WebRequest request){
        return adminRoleService.getRoles(request.getParameterMap());
    }

    @PostMapping(value = "/")
    public BaseApiResponse addRole(@RequestBody AdminRole role, HttpServletRequest request, Principal principal){
        return adminRoleService.addRole(role, request, principal);
    }

    @PutMapping(value = "/{id}")
    public BaseApiResponse updateRole(@RequestBody String roleName,
                                      HttpServletRequest request,
                                      @PathVariable long id,
                                      Principal principal){
        return adminRoleService.updateRole(roleName, id, request, principal);
    }

    @GetMapping(value = "/permissions/in/{roleId}")
    public BaseApiResponse getPermissionsInRole(@PathVariable long roleId, WebRequest request){
        return adminRoleService.getAllocatedPemissions(roleId,request.getParameterMap());
    }

    @GetMapping(value = "/permissions/notin/{roleId}")
    public BaseApiResponse getPermissionsNotInRole(@PathVariable long roleId, WebRequest request){
        return adminRoleService.getUnAllocatedPemissions(roleId,request.getParameterMap());
    }

    @PostMapping(value = "/allocate/{roleId}")
    public BaseApiResponse allocatePermissionsToRole(@PathVariable long roleId,
                                                     HttpServletRequest request,
                                                     @RequestBody String body,
                                                     Principal principal){

        return adminRoleService.allocatePermissions(body, roleId, request, principal);
    }

    @PostMapping(value = "/deallocate/{roleId}")
    public BaseApiResponse deAllocatePermissionsToRole(@PathVariable long roleId,
                                         HttpServletRequest request,
                                         @RequestBody String body,
                                         Principal principal){
        return adminRoleService.deallocatePermissions(body,roleId, request, principal);
    }



}
