package com.poosh.event.management.adminrolepermissions;

import javax.persistence.*;

@Entity(name = "AdminRolePermissions")
@Table(name = "admin_role_permissions")
public class AdminRolePermissions {

    @SequenceGenerator(
            name = "admin_role_permissions_sequence",
            sequenceName = "admin_role_permissions_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "admin_role_permissions_sequence"
    )

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Integer id;

    @Column(
            name = "role_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Integer role_id;

    @Column(
            name = "permission_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Integer permission_id;

    @Column(
            name = "allocated_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Integer allocated_by;

    @Column(
            name = "is_active",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private Boolean isActive;

    public AdminRolePermissions(){

    }
    public AdminRolePermissions(Integer role_id, Integer permission_id, Integer allocated_by) {
        this.role_id = role_id;
        this.permission_id = permission_id;
        this.allocated_by = allocated_by;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public Integer getPermission_id() {
        return permission_id;
    }

    public void setPermission_id(Integer permission_id) {
        this.permission_id = permission_id;
    }

    public Integer getAllocated_by() {
        return allocated_by;
    }

    public void setAllocated_by(Integer allocated_by) {
        this.allocated_by = allocated_by;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
