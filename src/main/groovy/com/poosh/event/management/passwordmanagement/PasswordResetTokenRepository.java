package com.poosh.event.management.passwordmanagement;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE PasswordResetToken token SET token.isActive = ?2 WHERE token.id = ?1")
    void updatePasswordResetTokenStatus(Long id, boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE PasswordResetToken token SET token.isActive = :status WHERE token.token = :token")
    void updatePasswordResetTokenStatusByToken(
            @Param("token")String token,
            @Param("status")boolean status
    );

    @Query(
            value = "SELECT token FROM password_reset_tokens WHERE user_id = :userId AND is_active = TRUE AND now() < (add_date + interval '1 day')",
            nativeQuery = true
    )
    String getValidToken(@Param("userId") Long userId);

    @Query(
            value = "SELECT token FROM password_reset_tokens WHERE token = :token AND is_active = TRUE AND now() < (add_date + interval '1 day')",
            nativeQuery = true
    )
    String checkTokenValidity(@Param("token") String token);

    @Query("SELECT token.userId FROM PasswordResetToken token WHERE token.token = :token")
    Long getUserIdByToken(@Param("token") String token);


}
