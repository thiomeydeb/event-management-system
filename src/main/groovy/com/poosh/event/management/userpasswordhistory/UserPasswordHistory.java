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
    private Integer id;

    @Column(
            name = "user_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Integer user_id;

    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "VARCHAR(256)"
    )
    private String password;

    @Column(
            name ="time_updated",
            nullable = false,
            columnDefinition = "TIMESTAMP(6) WITH TIME ZONE"
    )
    private LocalDate time_updated;

    public UserPasswordHistory(){

    }

    public UserPasswordHistory(
            Integer id,
            Integer user_id,
            String password,
            LocalDate time_updated) {
        this.id = id;
        this.user_id = user_id;
        this.password = password;
        this.time_updated = time_updated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getTime_updated() {
        return time_updated;
    }

    public void setTime_updated(LocalDate time_updated) {
        this.time_updated = time_updated;
    }
}