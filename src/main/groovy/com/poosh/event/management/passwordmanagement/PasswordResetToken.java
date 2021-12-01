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
    private Long userId;

    @Column(
            name ="addDate",
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


}
