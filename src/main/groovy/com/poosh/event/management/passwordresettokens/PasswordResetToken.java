package com.poosh.event.management.passwordresettokens;

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
    private Integer id;

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
    private Integer user_id;

    @Column(
            name ="add_date",
            nullable = false,
            columnDefinition = "TIMESTAMP(6) WITH TIME ZONE"
    )
    private LocalDate add_date;

    @Column(
            name ="is_active",
            updatable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private Boolean isActive;

    public PasswordResetToken(){

    }
    public PasswordResetToken(String token, Integer user_id, LocalDate add_date, Boolean isActive) {
        this.token = token;
        this.user_id = user_id;
        this.add_date = add_date;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public LocalDate getAdd_date() {
        return add_date;
    }

    public void setAdd_date(LocalDate add_date) {
        this.add_date = add_date;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
