package com.poosh.event.management.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


    @Repository
    public interface UsersRepository extends CrudRepository<Users, Long>{
        void updateUsersStatus(Long id, boolean status);
}
