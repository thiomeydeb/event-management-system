package com.poosh.event.management.userloginlogs

import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserLoginLogService {

    private final UserLoginLogRepository userLoginLogRepository

    @Autowired //inject the object dependency implicitly
    public UserLoginLogService(UserLoginLogRepository userLoginLogRepository) {
        this.userLoginLogRepository = userLoginLogRepository
    }

    void getAllUserLoginLog(){
        userLoginLogRepository.findAll()
    }

    def getUserLoginLogById(Long id){
        def UserLoginLog = userLoginLogRepository.findById(id)
        UserLoginLog;
    }
}
