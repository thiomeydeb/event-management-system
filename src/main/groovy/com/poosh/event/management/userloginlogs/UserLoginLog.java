package com.poosh.event.management.userloginlogs;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "UserLoginLog")
@Table(name = "user_login_log")
public class UserLoginLog {
    @SequenceGenerator(
            name = "user_login_log_sequence",
            sequenceName = "user_login_log_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_login_log_sequence"
    )
    @Id
    @Column(
            name = "id",
            updatable = false,
            columnDefinition = "BIGSERIAL"
    )
    private Long id;

    @Column(
            name = "ip_address",
            nullable = false,
            columnDefinition = "VARCHAR(250)"
    )
    private String ipAddress;

        @Column(
            name = "success",
            columnDefinition = "BOOLEAN"
    )
    private Long success;

    @Column(
            name ="entered_email",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private LocalDate enteredEmail;

    @Column(
            name = "login_time",
            nullable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP"
    )
    private String loginTime;

    public UserLoginLog(){

    }

    public UserLoginLog(String ipAddress, Long success, LocalDate enteredEmail, String loginTime) {
        this.ipAddress = ipAddress;
        this.success = success;
        this.enteredEmail = enteredEmail;
        this.loginTime = loginTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

    public LocalDate getEnteredEmail() {
        return enteredEmail;
    }

    public void setEnteredEmail(LocalDate enteredEmail) {
        this.enteredEmail = enteredEmail;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}
