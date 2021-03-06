package com.poosh.event.management.userpasswordhistory;

import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/password-history")
public class UserPasswordHistoryController {

    private final UserPasswordHistoryService userPasswordHistoryServices;

    @Autowired
    public UserPasswordHistoryController(UserPasswordHistoryService userPasswordHistoryService) {
        this.userPasswordHistoryServices = userPasswordHistoryService;
    }

    @GetMapping
    public void getAllUserPasswordHistory(){
        userPasswordHistoryServices.getAllUserPasswordHistory();
    }


    @GetMapping("/{id}")
    public Object getUserPasswordHistoryById(@PathVariable("id") Long id){
        return userPasswordHistoryServices.getUserPasswordHistoryById(id);
    }
}
