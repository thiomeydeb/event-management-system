package com.poosh.event.management.passwordresettokens

import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository

    @Autowired //inject the object dependency implicitly
    public PasswordResetTokenService(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository
    }

    void addPasswordResetToken (PasswordResetToken passwordResetToken){
        passwordResetTokenRepository.save(passwordResetToken)
    }

    void updatePasswordResetToken(PasswordResetToken passwordResetToken){
        passwordResetTokenRepository.save(passwordResetToken)
    }

    void updateStatus(Long id, boolean status){
        passwordResetTokenRepository.updatePasswordResetTokenStatus(id, status)
    }

    void getAllPasswordResetTokens(){
        passwordResetTokenRepository.findAll()
    }

    def getPasswordResetTokenById(Long id){
        def PasswordResetToken = passwordResetTokenRepository.findById(id)
        PasswordResetToken;
    }
}
