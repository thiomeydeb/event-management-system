package com.poosh.event.management.accountactivation;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "AccountActivationToken")
@Table(name = "account_activation_token")
public class AccountActivationToken {
    @SequenceGenerator(
            name = "account_activation_token_sequence",
            sequenceName = "account_activation_token_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_activation_token_sequence"
    )
    @Id
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "token",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private Long token;

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
            updatable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private Boolean isActive;

    public AccountActivationToken() {
    }

    public AccountActivationToken(Long token, Long userId, LocalDate addDate, Boolean isActive) {
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

    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
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
