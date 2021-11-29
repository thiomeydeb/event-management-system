package com.poosh.event.management.authentication

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class AjaxAuthenticationService {

    private final UserService userService

    @Autowired
    AjaxAuthenticationService(UserService userService) {
        this.userService = userService
    }

    BaseApiResponse generateAjaxLogInResults(boolean auth){
        int statusNum = auth ? 1 : 0;
        def res = [success:true,status:statusNum];
        return new BaseApiResponse(res, HttpStatus.OK.value(), "success", [])
    }

    BaseApiResponse userAuthenticationResults(boolean isAuthenticated, Authentication auth){
        int statusNum = isAuthenticated ? 1 : 0;
        def res = [success:true,status:statusNum];
        Map theUser = [:]
        if(isAuthenticated){
            theUser = userService.getUserDetailsByEmailMap(auth.getName());
            res.put("data",theUser);
        }
        Map data = ["user": theUser, "status": statusNum]
        return new BaseApiResponse(data, HttpStatus.OK.value(), "success", [])

    }
}
