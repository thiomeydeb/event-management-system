package com.poosh.event.management.passwordresettokens;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
    @Modifying
    @Query("UPDATE PasswordResetToken token SET token.isActive = ?2 WHERE token.id = ?1")
    void updatePasswordResetTokenStatus(Long id, boolean status);

}
