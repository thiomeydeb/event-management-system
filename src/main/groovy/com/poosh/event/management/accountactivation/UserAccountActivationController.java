package com.poosh.event.management.accountactivation;

import com.poosh.event.management.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("useraccount")
public class UserAccountActivationController {

    private final UserService userService;

    @Autowired
    public UserAccountActivationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/activate/{token}")
    public String activateAccount(Model model, @PathVariable String token, HttpServletRequest request){
        Long userId = userService.getUserIdFromToken(token);
        if(userId == null){
            model.addAttribute("message","The account activation link is not valid.");
            return "error";
        }else{
            boolean isTokenValid = userService.checkTokenValidity(token);
            if(isTokenValid){
                userService.activateMerchantAccount(userId,token);
                return "accountActivationSuccess";
            }else{
                userService.sendAccountActivationLink(userId);
                model.addAttribute("message","The account activation link has expired. Another link has been sent to your email");
                return "error";
            }

        }

    }
}
