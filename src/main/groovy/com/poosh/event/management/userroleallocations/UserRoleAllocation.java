package com.poosh.event.management.userroleallocations;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "UserRoleAllocation")
@Table(name = "user_role_allocation")
public class UserRoleAllocation{

    @SequenceGenerator(
            name = "User_Role_Allocation_sequence",
            sequenceName = "User_Role_Allocation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "User_Role_Allocation_sequence"

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
            name = "role_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Integer role_id;

    @Column(
            name = "time_allocation",
            nullable = false,
            columnDefinition = "TIMESTAMP(6) WITH TIME ZONE"
    )
    private LocalDate time_allocation;

    @Column(
            name = "allocated_by",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Integer allocated_by;

    public UserRoleAllocation(){

    }
    public UserRoleAllocation(
            Integer user_id,
            Integer role_id,
            LocalDate time_allocation,
            Integer allocated_by) {

        this.user_id = user_id;
        this.role_id = role_id;
        this.time_allocation = time_allocation;
        this.allocated_by = allocated_by;
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

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public LocalDate getTime_allocation() {
        return time_allocation;
    }

    public void setTime_allocation(LocalDate time_allocation) {
        this.time_allocation = time_allocation;
    }

    public Integer getAllocated_by() {
        return allocated_by;
    }

    public void setAllocated_by(Integer allocated_by) {
        this.allocated_by = allocated_by;
    }
}
