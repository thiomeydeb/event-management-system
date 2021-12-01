package com.poosh.event.management.passwordmanagement;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import com.poosh.event.management.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/password-reset-token")
public class PasswordManagementController {

    private final PasswordManagementService passwordManagementService;
    private final UserService userService;

    @Autowired
    public PasswordManagementController(PasswordManagementService passwordManagementService,
                                        UserService userService) {
        this.passwordManagementService = passwordManagementService;
        this.userService = userService;
    }

    @GetMapping(value = "/change/{token}")
    public String resetPassword(Model model, @PathVariable String token, HttpServletRequest request){
        boolean isTokenValid = passwordManagementService.checkTokenValidity(token);
        if(isTokenValid){
            model.addAttribute("header1Operation", "Change");
            model.addAttribute("header3Operation", "Update");
            return "updatePassword";
        }else{
            model.addAttribute("message","The reset password link has expired.");
            return "error";
        }
    }

    @GetMapping(value = "/set/{token}")
    public String setPassword(Model model, @PathVariable String token, HttpServletRequest request){
        boolean isTokenValid = passwordManagementService.checkTokenValidity(token);
        if(isTokenValid){
            model.addAttribute("header1Operation", "Set");
            model.addAttribute("header3Operation", "Set");
            return "updatePassword";
        }else{
            model.addAttribute("message","The set password link has expired.");
            return "error";
        }
    }

    @PostMapping(value = "/save")
    public String savePassword(WebRequest request, Model model, RedirectAttributes redirectAttributes){
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");
        String operation, expiryMessage = "";
        String formOperation = request.getParameter("operation");
        if(formOperation.equals("Set")){
            operation = "set";
            expiryMessage = "Verification link has expired. Kindly contact support to complete this process.";
        }else{
            operation = "reset";
            expiryMessage = "Password reset link has expired. Kindly begin the process again.";
        }

        boolean isTokenValid= passwordManagementService.checkTokenValidity(token);
        if(isTokenValid){
            if(password.equals(confirmPassword)){
                if(passwordManagementService.isPasswordStrong(password)){
                    long userId = passwordManagementService.getUserIdFromToken(token);
                    boolean isPasswordExist = passwordManagementService.checkPasswordHistory(userId,password);
                    if(isPasswordExist){
                        String referer = request.getHeader("Referer");
                        redirectAttributes.addFlashAttribute("errorMessage","Password already used. Please type a new password");
                        return "redirect:"+ referer;
                    }else{
                        boolean isPasswordSaved = passwordManagementService.savePassword(userId, password);
                        if(isPasswordSaved){
                            passwordManagementService.updateTokenStatus(token, false);
                            if(operation.equals("set")){
                                userService.updateUserStatus(userId);
                            }
                            model.addAttribute("successMessage", "Your password has successfully been "+operation);
                            return "passwordResetSuccess";
                        }else{
                            model.addAttribute("message","Failed to "+operation+" password ");
                            return "error";
                        }
                    }
                }else{
                    String referer = request.getHeader("Referer");
                    redirectAttributes.addFlashAttribute("errorMessage","The password must have at least one uppercase letter, one lowercase letter, one number, one special character and should have at least eight characters in length");
                    return "redirect:"+ referer;
                }


            }else{
                String referer = request.getHeader("Referer");
                redirectAttributes.addFlashAttribute("errorMessage","Password Mismatch");
                return "redirect:"+ referer;
            }
        }else{
            model.addAttribute("message", expiryMessage);
            return "error";
        }
    }

    @RequestMapping(value = "/savenew",method = RequestMethod.POST)
    public String saveNewAccountPassword(WebRequest request, Model model, RedirectAttributes redirectAttributes){
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");

        boolean isTokenValid = passwordManagementService.checkTokenValidity(token);
        if(isTokenValid){
            if(password.equals(confirmPassword)){
                if(passwordManagementService.isPasswordStrong(password)){
                    long userId = passwordManagementService.getUserIdFromToken(token);
                    boolean isPasswordSaved = passwordManagementService.savePassword(userId, password);
                    if(isPasswordSaved){
                        passwordManagementService.updateTokenStatus(token, false);
                        model.addAttribute("successMessage", "Your password has successfully been set.");
                        return "passwordResetSuccess";
                    }else{
                        model.addAttribute("message","Failed to set password");
                        return "error";
                    }
                }else{
                    String referer = request.getHeader("Referer");
                    redirectAttributes.addFlashAttribute("errorMessage","The password must have at least one uppercase letter, one lowercase letter, one number, one special character and should have at least eight characters in length");
                    return "redirect:"+ referer;
                }
            }else{
                String referer = request.getHeader("Referer");
                redirectAttributes.addFlashAttribute("errorMessage","Password Mismatch");
                return "redirect:"+ referer;
            }
        }else{
            model.addAttribute("message","Verification link has expired. Kindly contact support to complete this process.");
            return "error";
        }
    }

    @PostMapping(value = "/reset")
    @ResponseBody
    public BaseApiResponse passwordResetRequest(WebRequest request, HttpServletRequest servletRequest){
        return passwordManagementService.passwordResetRequest(request, servletRequest);
    }

    @PostMapping(value = "/update")
    @ResponseBody
    public BaseApiResponse changePassword(WebRequest request, HttpServletRequest servletRequest) {
        return passwordManagementService.changePassword(request, servletRequest);
    }
}
