package com.poosh.event.management.passwordmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/password-reset-token")
public class PasswordManagementController {

    private final PasswordManagementService passwordManagementService;

    @Autowired
    public PasswordManagementController(PasswordManagementService passwordManagementService) {
        this.passwordManagementService = passwordManagementService;
    }

    @GetMapping
    public void getAllPasswordResetTokens(){
        passwordManagementService.getAllPasswordResetTokens();
    }


    @GetMapping("/{id}")
    public Object getPasswordResetTokenById(@PathVariable("id") Long id){
        return passwordManagementService.getPasswordResetTokenById(id);
    }

//    @PostMapping
//    public void addPasswordResetToken(@RequestBody EventType body){
//        passwordResetTokenService.addPasswordResetToken (body);
//    }
}
