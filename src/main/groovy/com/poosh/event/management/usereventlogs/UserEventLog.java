package com.poosh.event.management.usereventlogs;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "UserEventLog")
@Table(name = "user_event_log")
public class UserEventLog {
    @SequenceGenerator(
            name = "user_event_log_sequence",
            sequenceName = "user_event_log_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_event_log_sequence"
    )
    @Id
    @Column(
            name = "id",
            updatable = false,
            columnDefinition = "BIGSERIAL"
    )
    private Long id;

    @Column(
            name = "action",
            nullable = false,
            columnDefinition = "VARCHAR(250)"
    )
    private String action;

    @Column(
            name = "ip_address",
            nullable = false,
            columnDefinition = "VARCHAR(50)"
    )
    private String ipAddress;

    @Column(
            name = "user_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Long userId;

    @Column(
            name ="transaction_time",
            nullable = false,
            columnDefinition = "TIMESTAMP(6) WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDate transactionTime;

    @Column(
            name = "reference",
            nullable = false,
            columnDefinition = "VARCHAR(50)"
    )
    private String reference;

    public UserEventLog() {
    }

    public UserEventLog(String action, String ipAddress, Long userId, LocalDate transactionTime, String reference) {
        this.action = action;
        this.ipAddress = ipAddress;
        this.userId = userId;
        this.transactionTime = transactionTime;
        this.reference = reference;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDate transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
