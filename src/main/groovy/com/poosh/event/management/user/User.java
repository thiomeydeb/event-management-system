package com.poosh.event.management.user;

import javax.persistence.*;

@Entity(name = "User")
@Table(name = "users")
public class User {


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
    private String firstName;

    @Column(
            name = "middle_name",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private String middleName;

    @Column(
            name = "last_name",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private String lastName;

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
    private String phoneNumber;

    @Column(
            name = "identification_type",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private String identificationType;

    @Column(
            name = "identification_number",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private String identificationNumber;

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
    private String companyName;

    public User(){
    }
    public User(
            String firstName,
            String middleName,
            String lastName,
            String password,
            String email,
            String phoneNumber,
            String identificationType,
            String identificationNumber,
            Boolean isActive,
            String companyName) {

        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.identificationType = identificationType;
        this.identificationNumber = identificationNumber;
        this.isActive = isActive;
        this.companyName = companyName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
