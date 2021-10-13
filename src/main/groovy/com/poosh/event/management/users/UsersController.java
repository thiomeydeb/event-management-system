package com.poosh.event.management.users;

import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {

    private final UsersService usersService;


    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public void getAllUsers(){
        usersService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Object getUsersById(@PathVariable("id") Long id){
        return usersService.getUsersById(id);
    }

    @PostMapping
    public void addUsers(@RequestBody Users body){
        usersService.addUsers(body);
    }

}
