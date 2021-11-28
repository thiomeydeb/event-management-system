package com.poosh.event.management.userpasswordhistory;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "UserPasswordHistory")
@Table(name = "user_password_history")
public class UserPasswordHistory {

    @SequenceGenerator(
            name = "user_password_history_sequence",
            sequenceName = "user_password_history_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_password_history_sequence"
    )

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Long id;

    @Column(
            name = "user_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Long userId;

    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "VARCHAR(256)"
    )
    private String password;

    @Column(
            name ="time_updated",
            nullable = false,
            columnDefinition = "TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDate timeUpdated;

    public UserPasswordHistory(){

    }

    public UserPasswordHistory(Long userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public UserPasswordHistory(Long id, Long userId, String password, LocalDate timeUpdated) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.timeUpdated = timeUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(LocalDate timeUpdated) {
        this.timeUpdated = timeUpdated;
    }
}