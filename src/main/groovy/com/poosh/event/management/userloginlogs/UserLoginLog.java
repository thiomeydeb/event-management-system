package com.poosh.event.management.userloginlogs;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "UserLoginLog")
@Table(name = "user_login_log")
public class UserLoginLog {
    @SequenceGenerator(
            name = "user_login_log_sequence",
            sequenceName = "user_login_log_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_login_log_sequence"
    )

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Integer id;

    @Column(
            name = "action",
            nullable = false,
            columnDefinition = "VARCHAR(250)"
    )
    private String action;

    @Column(
            name = "ip_address",
            nullable = false,
            columnDefinition = "VARCHAR(250)"
    )
    private String ip_address;

        @Column(
            name = "user_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Integer user_id;

    @Column(
            name ="transaction_time",
            nullable = false,
            columnDefinition = "TIMESTAMP(6) WITHOUT TIME ZONE"
    )
    private LocalDate transaction_time;

    @Column(
            name = "reference",
            nullable = false,
            columnDefinition = "VARCHAR(50)"
    )
    private String reference;

    public UserLoginLog(){

    }

    public UserLoginLog(
            String action,
            String ip_address,
            Integer user_id,
            LocalDate transaction_time,
            String reference) {
        this.action = action;
        this.ip_address = ip_address;
        this.user_id = user_id;
        this.transaction_time = transaction_time;
        this.reference = reference;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public LocalDate getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(LocalDate transaction_time) {
        this.transaction_time = transaction_time;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
