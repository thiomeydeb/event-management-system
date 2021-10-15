package com.poosh.event.management.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
    public interface UsersRepository extends CrudRepository<Users, Long>{
        void updateUsersStatus(Long id, boolean status);
        Optional<Users> findUsersByCompany_nameAndActiveOrderById(String email);
}
