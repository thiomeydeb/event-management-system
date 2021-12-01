package com.poosh.event.management.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Modifying
    @Query("UPDATE User user SET user.isActive = ?2 WHERE user.id = ?1")
    int updateUserStatus(Long id, boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.password = :password WHERE user.id = :userId")
    int updateUserPassword(
            @Param("userId") Long userId,
            @Param("password") String password
    );

    Optional<User> findUserByEmailEquals(String email);

    @Query("SELECT user.password FROM User user WHERE user.id = :userId")
    String getCurrentUserPassword(@Param("userId") Long userId);
}
