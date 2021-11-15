package com.poosh.event.management.users;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {

    @Modifying
    @Query("UPDATE Users users SET users.isActive = ?2 WHERE users.id = ?1")
    void updateUsersStatus(Long id, boolean status);

    Optional<Users> findUsersByEmailOrderById(String email);
}
