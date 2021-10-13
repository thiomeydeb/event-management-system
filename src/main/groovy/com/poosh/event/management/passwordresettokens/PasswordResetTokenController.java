package com.poosh.event.management.passwordresettokens;

import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PasswordResetTokenController {

    private final PasswordResetTokenService passwordResetTokenService;

    @Autowired
    public PasswordResetTokenController(PasswordResetTokenService passwordResetTokenService) {
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @GetMapping
    public void getAllPasswordResetTokens(){
        passwordResetTokenService.getAllPasswordResetTokens();
    }


    @GetMapping("/{id}")
    public Object getPasswordResetTokenById(@PathVariable("id") Long id){
        return passwordResetTokenService.getPasswordResetTokenById(id);
    }

//    @PostMapping
//    public void addPasswordResetToken(@RequestBody EventType body){
//        passwordResetTokenService.addPasswordResetToken (body);
//    }
}
