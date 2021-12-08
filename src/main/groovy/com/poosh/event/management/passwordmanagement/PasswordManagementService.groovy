package com.poosh.event.management.passwordmanagement

import com.poosh.event.management.apiresponse.BaseApiResponse
import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.exceptions.BadRequestException
import com.poosh.event.management.exceptions.InternalServerErrorException
import com.poosh.event.management.user.User
import com.poosh.event.management.user.UserRepository
import com.poosh.event.management.userpasswordhistory.UserPasswordHistory
import com.poosh.event.management.userpasswordhistory.UserPasswordHistoryRepository
import com.poosh.event.management.utils.Constant
import com.poosh.event.management.utils.EmailSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.context.request.WebRequest

import javax.servlet.http.HttpServletRequest
import java.time.LocalDate

@Service
class PasswordManagementService {

    private final PasswordResetTokenRepository passwordResetTokenRepository
    private final UserRepository userRepository
    private final PasswordEncoder passwordEncoder
    private final UserPasswordHistoryRepository userPasswordHistoryRepository
    private final EmailSender emailSender

    @Autowired
    //inject the object dependency implicitly
    PasswordManagementService(PasswordResetTokenRepository passwordResetTokenRepository,
                              UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              UserPasswordHistoryRepository userPasswordHistoryRepository,
                              EmailSender emailSender) {
        this.passwordResetTokenRepository = passwordResetTokenRepository
        this.userRepository = userRepository
        this.passwordEncoder = passwordEncoder
        this.userPasswordHistoryRepository = userPasswordHistoryRepository
        this.emailSender = emailSender
    }

    boolean insertPasswordToken(Long userId, String token) {
        def res = false;
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, userId, LocalDate.now(), true)
        def savedToken = passwordResetTokenRepository.save(passwordResetToken)
        if (savedToken) {
            res = true;
        }
        res
    }

    String getValidToken(Long userId) {
        return passwordResetTokenRepository.getValidToken(userId)
    }

    boolean checkTokenValidity(String token) {
        def resQuery = passwordResetTokenRepository.checkTokenValidity(token)
        if (resQuery) {
            return true;
        } else {
            return false;
        }
    }

    long getUserIdFromToken(String token) {
        return passwordResetTokenRepository.getUserIdByToken(token);
    }

    boolean savePassword(long userId, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        int passwordUpdate = userRepository.updateUserPassword(userId, hashedPassword)
        if (passwordUpdate == 1) {
            insertPasswordHistory(userId, hashedPassword);
            return true;
        } else {
            return false;
        }


    }

    boolean insertPasswordHistory(long userId, String hashedPassword) {
        UserPasswordHistory passwordHistory = new UserPasswordHistory(userId, hashedPassword)
        def savedPasswordHistory = userPasswordHistoryRepository.save(passwordHistory)
        if (savedPasswordHistory) {
            return true;
        } else {
            return false;
        }
    }

    void updateTokenStatus(String token, boolean status) {
        passwordResetTokenRepository.updatePasswordResetTokenStatusByToken(token, status)
    }

    boolean checkPasswordHistory(long userId, String password) {
        def optionalPasswordHistory = userPasswordHistoryRepository.findByUserId(userId)
        if (optionalPasswordHistory.isPresent()) {
            def passwordHistory = optionalPasswordHistory.get()
            def matchingPassword = passwordEncoder.matches(password, passwordHistory.getPassword())
            if (matchingPassword) {
                return true;
            } else {
                return false;
            }
        }
        return false
    }

    boolean checkCurrentPassword(long userId, String password) {
        def isMatch = false;
        def currentUserPassword = userRepository.getCurrentUserPassword(userId)
        isMatch = passwordEncoder.matches(password, currentUserPassword);
        return isMatch;
    }

    boolean isPasswordStrong(String password) {
        return password.matches("^.*(?=.{8,})((?=.*[!@#\$%^&*()\\-_=+{};:,<.>]){1})(?=.*\\d)((?=.*[a-z]){1})((?=.*[A-Z]){1}).*\$")
    }

    BaseApiResponse changePassword(WebRequest request, HttpServletRequest servletRequest) {
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "success")

        String currentPassword = request.getParameter("currentPassword")
        String newPassword = request.getParameter("newPassword")
        String confirmPassword = request.getParameter("confirmPassword")
        String email = request.getParameter("email")

        def optionalUser = userRepository.findUserByEmailEquals(email)
        if (!optionalUser.isPresent()) {
            throw new BadRequestException(String.format("user with email %s does not exist", email))
        }
        def user = optionalUser.get()
        long userId = user.getId()
        boolean isOldPasswordExist = checkCurrentPassword(userId, currentPassword)
        if (isOldPasswordExist) {
            if (newPassword.equals(confirmPassword)) {
                if (isPasswordStrong(newPassword)) {
                    boolean isPasswordExist = checkPasswordHistory(userId, newPassword)
                    if (isPasswordExist) {
                        throw new BadRequestException("Password already used. Please type a new password")
                    } else {
                        boolean isPasswordSaved = savePassword(userId, newPassword)
                        if (isPasswordSaved) {
                            res.setMessage("Password update successful")
                            return res
                        } else {
                            throw new InternalServerErrorException("Password reset failed")
                        }
                    }
                } else {
                    throw new BadRequestException("Weak Password")
                }
            } else {
                throw new BadRequestException("New Password and Confirm password do not match")
            }
        } else {
            throw new BadRequestException("The current password is wrong")
        }

    }

    BaseApiResponse passwordResetRequest(WebRequest request, HttpServletRequest servletRequest) {
        BaseApiResponse res = new BaseApiResponse(HttpStatus.OK.value(), "success")
        boolean status = false;
        String email = request.getParameter("email");
        String token;
        def optionalUser = userRepository.findUserByEmailEquals(email)
        if (!optionalUser.isPresent()) {
            throw new BadRequestException(String.format("user with email %s does not exist", email))
        }
        def user = optionalUser.get()
        long userId = user.getId()
        token = getValidToken(userId)
        String appUrl = servletRequest.getScheme()+"://"+servletRequest.getServerName()+":"+servletRequest.getLocalPort()+servletRequest.getContextPath()+servletRequest.getServletPath()+"/password/change/"
        if(token == null){
            String tokenStr = UUID.randomUUID().toString()
            status = insertPasswordToken(userId,tokenStr)
            if(status){
                emailSender.sendMail(
                        Constant.FROM_EMAIL, email,
                        Constant.PASSWORD_RESET_SUBJECT,
                        Constant.PASSWORD_RESET_MESSAGE+appUrl+tokenStr+ Constant.REGARDS_MESSAGE
                )
                res.setMessage("Request successful")
            }else{
                throw new InternalServerErrorException("Request not successful")
            }
        } else {
            emailSender.sendMail(
                    Constant.FROM_EMAIL,
                    email,
                    Constant.PASSWORD_RESET_SUBJECT,
                    Constant.PASSWORD_RESET_MESSAGE + appUrl + token + Constant.REGARDS_MESSAGE
            )
            res.setMessage("Request successful")
            return res
        }
    }
}
