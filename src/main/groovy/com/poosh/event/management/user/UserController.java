package com.poosh.event.management.user;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import com.poosh.event.management.passwordmanagement.PasswordManagementService;
import com.poosh.event.management.user.dto.UserCreateDto;
import com.poosh.event.management.user.dto.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import com.poosh.event.management.utils.CommonDbFunctions;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/user")
@Validated
public class UserController {

    private final UserService userService;
    private final PasswordManagementService passwordManagementService;


    @Autowired
    public UserController(UserService userService, PasswordManagementService passwordManagementService) {
        this.userService = userService;
        this.passwordManagementService = passwordManagementService;
    }

    @GetMapping(value = "")
    public BaseApiResponse getUsers(WebRequest request){
        return userService.getUsers(request.getParameterMap());
    }

    @GetMapping(value = "/byrole/{roleId}")
    public BaseApiResponse getUsersBasedOnRole(WebRequest request, @PathVariable int roleId){
        return userService.getUsersBasedOnRole(request.getParameterMap(), roleId);
    }

    @PostMapping(value = "")
    public BaseApiResponse addUser(@RequestBody @Valid UserCreateDto user){
        return userService.addUser(user,1);
    }

    @PutMapping(value = "/{userId}")
    public BaseApiResponse editUser(@RequestBody @Valid UserUpdateDto user, @PathVariable long userId){
        return userService.updateUser(userId, user);
    }

    @PostMapping(value ="/changestatus")
    public BaseApiResponse changeStatus(Model model, WebRequest request){
        int userId = Integer.parseInt(request.getParameter("userId"));
        boolean status = Boolean.parseBoolean(request.getParameter("status"));
        return userService.changeStatus(userId,status);
    }

    @PostMapping(value ="/resendactivationlink")
    public BaseApiResponse resendActivationLink(WebRequest request){
        int userId = Integer.parseInt(request.getParameter("userId"));
        userService.sendAccountActivationLink(userId);
        return new BaseApiResponse(1,"Account activation link Sent");
    }

    @PostMapping(value ="/promote")
    public BaseApiResponse promoteToAdmin(WebRequest request, Principal principal){
        String currentUser  = principal.getName();
        long loggedInUserId = CommonDbFunctions.getUserIdFromEmail(currentUser);

        int userId = Integer.parseInt(request.getParameter("userId"));
        return userService.promoteToAdmin(userId,loggedInUserId);
    }

    @PostMapping(value ="/demote")
    public BaseApiResponse demoteFromAdmin(WebRequest request, Principal principal){
        String currentUser  = principal.getName();
        long loggedInUserId = CommonDbFunctions.getUserIdFromEmail(currentUser);

        int userId = Integer.parseInt(request.getParameter("userId"));
        return userService.demoteFromAdmin(userId,loggedInUserId);
    }

    @GetMapping(value = "/role")
    public BaseApiResponse getPermissionsInRole(@RequestParam long userId){
        return userService.getAllocatedUserRole(userId);
    }

    @PostMapping(value ="/assignrole")
    public BaseApiResponse assignRole(WebRequest request, Principal principal){
        String currentUser  = principal.getName();
        long loggedInUserId = CommonDbFunctions.getUserIdFromEmail(currentUser);
        long userId = Long.parseLong(request.getParameter("userId"));
        long roleId = Long.parseLong(request.getParameter("roleId"));
        return userService.assignRole(userId,roleId,loggedInUserId);
    }

    @PostMapping(value ="/deallocaterole")
    public BaseApiResponse deallocateRole(WebRequest request, Principal principal){
        String currentUser  = principal.getName();
        long loggedInUserId = CommonDbFunctions.getUserIdFromEmail(currentUser);
        long userId = Long.parseLong(request.getParameter("userId"));
        long roleId = Long.parseLong(request.getParameter("roleId"));
        return userService.deallocateRole(userId,roleId,loggedInUserId);
    }

    @PostMapping (value = "/registeruser")
    public BaseApiResponse registerUser(@RequestBody UserCreateDto user){
        return userService.registerUser(user);
    }

    @GetMapping(value = "/menus/{userId}")
    public BaseApiResponse getAllowedMenuItems(@PathVariable long userId) {
        return userService.getMenuDetails(userId);
    }

    @PostMapping(value = "/updateprofile")
    public BaseApiResponse updateProfile(@RequestBody String body, Principal principal){
        String userName = principal.getName();
        long userId = CommonDbFunctions.getUserIdFromEmail(userName);
        return userService.updateProfile(userId, body);

    }

}
