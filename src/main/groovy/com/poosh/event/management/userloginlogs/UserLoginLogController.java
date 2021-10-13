package com.poosh.event.management.userloginlogs;

import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserLoginLogController {

    private final UserLoginLogService userLoginLogService;

    @Autowired
    public UserLoginLogController(UserLoginLogService userLoginLogService) {
        this.userLoginLogService = userLoginLogService;
    }

    @GetMapping
    public void getAllUserLoginLog(){
        userLoginLogService.getAllUserLoginLog();
    }


    @GetMapping("/{id}")
    public Object getUserLoginLogById(@PathVariable("id") Long id){
        return userLoginLogService.getUserLoginLogById(id);
    }

    @PostMapping
    public void addUserLoginLog(@RequestBody UserLoginLog body){
        userLoginLogService.addUserLoginLog(body);
    }
}
