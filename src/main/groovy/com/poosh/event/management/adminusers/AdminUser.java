package com.poosh.event.management.adminusers;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "AdminUser")
@Table(name = "admin_user")
public class AdminUser {


    @SequenceGenerator(
            name = "admin_user_sequence",
            sequenceName = "admin_user_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "admin_user_sequence"
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
            name ="date_added",
            nullable = false,
            columnDefinition = "TIMESTAMP(6) WITH TIME ZONE"
    )
    private LocalDate date_added;

    @Column(
            name = "added_by",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Integer added_by;

    @Column(
            name ="is_active",
            updatable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private Boolean isActive;

    public AdminUser(){

    }

    public AdminUser (Integer user_id, LocalDate date_added, Integer added_by, Boolean isActive) {
        this.user_id = user_id;
        this.date_added = date_added;
        this.added_by = added_by;
        this.isActive = isActive;
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

    public LocalDate getDate_added() {
        return date_added;
    }

    public void setDate_added(LocalDate date_added) {
        this.date_added = date_added;
    }

    public Integer getAdded_by() {
        return added_by;
    }

    public void setAdded_by(Integer added_by) {
        this.added_by = added_by;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
