package com.poosh.event.management.authentication;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class AjaxAuthenticationController {

    private final AjaxAuthenticationService ajaxAuthenticationService;

    public AjaxAuthenticationController(AjaxAuthenticationService ajaxAuthenticationService) {
        this.ajaxAuthenticationService = ajaxAuthenticationService;
    }

    @GetMapping(value="/logout")
    public BaseApiResponse logoutPage (HttpServletRequest request, HttpServletResponse response, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUserAuthenticated = auth != null;
        if (isUserAuthenticated){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ajaxAuthenticationService.generateAjaxLogInResults(isUserAuthenticated);
    }

    @GetMapping(value="/isloggedin")
    public BaseApiResponse isUserAuthenticated () {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUserAuthenticated = (auth != null &&
                auth.isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken));

        return ajaxAuthenticationService.userAuthenticationResults(isUserAuthenticated,auth);
    }
}
