package com.poosh.event.management.adminroles;

import javax.persistence.*;

@Entity(name = "AdminRole")
@Table(name = "admin_role")
public class AdminRole {

    @SequenceGenerator(
            name = "admin_role_sequence",
            sequenceName = "admin_role_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "admin_role_sequence"
    )

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Integer id;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private String name;

    public AdminRole(){

    }
    public AdminRole(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
