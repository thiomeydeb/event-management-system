package com.poosh.event.management.users

import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UsersService {
    private final UsersRepository usersRepository

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository
    }

    void addUsers (Users users){
        usersRepository.save(users)
    }

    void updateUsers (Users users){
        usersRepository.save(users)
    }
    void updateStatus(Long id, boolean status){
        usersRepository.updateUsersStatus(id, status)
    }
    void getAllUsers(){
        usersRepository.findAll()
    }
    def getUsersById(Long id){
        def event = usersRepository.findById(id)
        event;
    }


}
