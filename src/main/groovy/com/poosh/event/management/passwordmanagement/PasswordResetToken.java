package com.poosh.event.management.passwordmanagement;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "PasswordResetToken")
@Table(name = "password_reset_token")
public class PasswordResetToken {

    @SequenceGenerator(
            name = "password_reset_token_sequence",
            sequenceName = "password_reset_token_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "password_reset_token_sequence"
    )

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Long id;

    @Column(
            name = "token",
            nullable = false,
            columnDefinition = "VARCHAR(200)"
    )
    private String token;

    @Column(
            name = "user_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Long userId;

    @Column(
            name ="add_date",
            nullable = false,
            columnDefinition = "TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDate addDate;

    @Column(
            name ="is_active",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private Boolean isActive;

    public PasswordResetToken(){

    }
    public PasswordResetToken(String token, Long userId, Boolean isActive) {
        this.token = token;
        this.userId = userId;
        this.isActive = isActive;
    }

    public PasswordResetToken(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    public PasswordResetToken(String token, Long userId, LocalDate addDate, Boolean isActive) {
        this.token = token;
        this.userId = userId;
        this.addDate = addDate;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getAddDate() {
        return addDate;
    }

    public void setAddDate(LocalDate addDate) {
        this.addDate = addDate;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
