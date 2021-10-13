package com.poosh.event.management.passwordresettokens;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
    void updatePasswordResetTokenStatus(Long id, boolean status);

}
