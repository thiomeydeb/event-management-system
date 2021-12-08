package com.poosh.event.management.audit;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("api/v1/audit")
public class AuditController {
    private final AuditService auditService;

    @Autowired
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping(value = "login/logs")
    public BaseApiResponse getLoginLogs(WebRequest request){
        return auditService.getLogInLogs(request.getParameterMap());
    }

    @GetMapping(value = "event/logs")
    public BaseApiResponse getEventLogs(WebRequest request){
        return auditService.getEventLogs(request.getParameterMap());
    }

    @GetMapping(value = "logs")
    public BaseApiResponse getLogs(){
        return auditService.getLogs();
    }
}
