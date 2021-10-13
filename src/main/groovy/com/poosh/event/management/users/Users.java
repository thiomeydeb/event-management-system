package com.poosh.event.management.users;

import javax.persistence.*;

@Entity(name = "Users")
@Table(name = "users")
public class Users {


    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Id
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @Column(
            name = "first_name",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private String first_name;

    @Column(
            name = "middle_name",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private String middle_name;

    @Column(
            name = "last_name",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private String last_name;

    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "VARCHAR(250)"
    )
    private String password;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private String email;

    @Column(
            name = "phone_number",
            nullable = false,
            columnDefinition = "VARCHAR(30)"
    )
    private String phone_number;

    @Column(
            name = "identification_type",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private String identification_type;

    @Column(
            name = "identification_number",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private String identification_number;

    @Column(
            name = "is_active",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private Boolean isActive;

    @Column(
            name = "company_name",
            nullable = false,
            columnDefinition = "VARCHAR(256)"
    )
    private String company_name;

    public Users(){
    }
    public Users(
            String first_name,
            String middle_name,
            String last_name,
            String password,
            String email,
            String phone_number,
            String identification_type,
            String identification_number,
            Boolean isActive,
            String company_name) {

        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.password = password;
        this.email = email;
        this.phone_number = phone_number;
        this.identification_type = identification_type;
        this.identification_number = identification_number;
        this.isActive = isActive;
        this.company_name = company_name;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getIdentification_type() {
        return identification_type;
    }

    public void setIdentification_type(String identification_type) {
        this.identification_type = identification_type;
    }

    public String getIdentification_number() {
        return identification_number;
    }

    public void setIdentification_number(String identification_number) {
        this.identification_number = identification_number;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
