package com.poosh.event.management.passwordmanagement

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.user.UserRepository
import com.poosh.event.management.userpasswordhistory.UserPasswordHistory
import com.poosh.event.management.userpasswordhistory.UserPasswordHistoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordManagementService {

    private final PasswordResetTokenRepository passwordResetTokenRepository
    private final UserRepository userRepository
    private final PasswordEncoder passwordEncoder
    private final UserPasswordHistoryRepository userPasswordHistoryRepository

    @Autowired //inject the object dependency implicitly
    PasswordManagementService(PasswordResetTokenRepository passwordResetTokenRepository,
                              UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              UserPasswordHistoryRepository userPasswordHistoryRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository
        this.userRepository = userRepository
        this.passwordEncoder = passwordEncoder
        this.userPasswordHistoryRepository = userPasswordHistoryRepository
    }

    boolean insertPasswordToken(Long userId,String token){
        def res = false;
        PasswordResetToken passwordResetToken = new PasswordResetToken(userId, token)
        def savedToken = passwordResetTokenRepository.save(passwordResetToken)
        if(savedToken){
            res = true;
        }
        res
    }

    String getValidToken(Long userId){
        return passwordResetTokenRepository.getValidToken(userId)
    }

    public boolean checkTokenValidity(String token){
        def resQuery = passwordResetTokenRepository.checkTokenValidity(token)
        if(resQuery){
            return true;
        }else{
            return false;
        }
    }

    public long getUserIdFromToken(String token){
        return passwordResetTokenRepository.getUserIdByToken(token);
    }

    public boolean savePassword(long userId,String password){
        String hashedPassword = passwordEncoder.encode(password);
        int passwordUpdate = userRepository.updateUserPassword(userId, hashedPassword)
        if(passwordUpdate == 1){
            insertPasswordHistory(userId, hashedPassword);
            return true;
        }else{
            return false;
        }


    }

    public boolean insertPasswordHistory(long userId,String hashedPassword){
        UserPasswordHistory passwordHistory = new UserPasswordHistory( userId, hashedPassword)
        def savedPasswordHistory = userPasswordHistoryRepository.save(passwordHistory)
        if(savedPasswordHistory){
            return true;
        }else{
            return false;
        }
    }

    public void updateTokenStatus(String token, boolean status){
        passwordResetTokenRepository.updatePasswordResetTokenStatusByToken(token, status)
    }

    public boolean checkPasswordHistory(long userId, String password){
        def optionalPasswordHistory = userPasswordHistoryRepository.findByUserId(userId)
        if(optionalPasswordHistory.isPresent()){
            def passwordHistory = optionalPasswordHistory.get()
            def matchingPassword = passwordEncoder.matches(password, passwordHistory.getPassword())
            if(matchingPassword){
                return true;
            }else{
                return false;
            }
        }
        return false
    }

    public boolean checkCurrentPassword(long userId, String password){
        def isMatch = false;
        def currentUserPassword = userRepository.getCurrentUserPassword(userId)
        isMatch = passwordEncoder.matches(password, currentUserPassword);
        return isMatch;
    }

    public boolean isPasswordStrong(String password){
        return  password.matches("^.*(?=.{8,})((?=.*[!@#\$%^&*()\\-_=+{};:,<.>]){1})(?=.*\\d)((?=.*[a-z]){1})((?=.*[A-Z]){1}).*\$")
    }
}
