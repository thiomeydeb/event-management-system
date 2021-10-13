package com.poosh.event.management.userpasswordhistory

import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
import com.poosh.event.management.userloginlogs.UserLoginLogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserPasswordHistoryService {

    private final UserPasswordHistoryRepository userPasswordHistoryRepository

    @Autowired //inject the object dependency implicitly
    public UserPasswordHistoryService(UserPasswordHistoryRepository userPasswordHistoryRepository) {
        this.userPasswordHistoryRepository = userPasswordHistoryRepository
    }

    void addUserPasswordHistory (UserPasswordHistory userPasswordHistory){
        userPasswordHistoryRepository.save(userPasswordHistory)
    }

    void updateUserPasswordHistory(UserPasswordHistory userPasswordHistory){
        userPasswordHistoryRepository.save(userPasswordHistory)
    }

    void updateStatus(Long id, boolean status){
        userPasswordHistoryRepository.updateUserPasswordHistoryStatus(id, status)
    }

    void getAllUserPasswordHistory(){
        userPasswordHistoryRepository.findAll()
    }

    def getUserPasswordHistoryById(Long id){
        def UserPasswordHistory = userPasswordHistoryRepository.findById(id)
        UserPasswordHistory;
    }
}
