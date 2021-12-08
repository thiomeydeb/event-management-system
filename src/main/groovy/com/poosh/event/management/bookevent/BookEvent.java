package com.poosh.event.management.bookevent;

import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "BookedEvent")
@Table(name = "booked_event")
public class BookEvent {


    @SequenceGenerator(
            name = "booked_event_sequence",
            sequenceName = "booked_event_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "booked_event_sequence"
    )
    @Id
    @Column(
            name = "id",
            updatable = false,
            columnDefinition = "BIGSERIAL"
    )
    private Long id;

    @Column(
            name = "title",
            nullable = false,
            columnDefinition = "VARCHAR(200)"
    )
    private String title;

//    @Column(
//            name = "user_id",
//            nullable = false,
//            columnDefinition = "BIGINT"
//    )
    @ManyToOne
    @JoinColumn(
            name = "client_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_booked_event_fk")
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "event_type_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "event_type_booked_event_fk")

    )
    private EventType eventType;

    @Column(
            name = "management_amount",
            nullable = false,
            columnDefinition = "DOUBLE PRECISION"
    )
    private Double management_amount;

    @Column(
            name = "status",
            columnDefinition = "INTEGER"
    )
    private Integer status;

    @Column(
            name = "other_information",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String other_information;

    @Column(
            name = "attendees",
            nullable = false,
            columnDefinition = "INTEGER"
    )
    private Integer attendees;

    @Column(
            name ="total_amount",
            nullable = false,
            columnDefinition = "DOUBLE PRECISION"
    )
    private Double total_amount;

    @Column(
            name ="timestamp",
            nullable = false,
            columnDefinition = "TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDate timestamp;

    @Column(
            name ="completion_timestamp",
            columnDefinition = "TIMESTAMP(6) WITH TIME ZONE"
    )
    private LocalDate completion_timestamp;

    @Column(
            name = "greening_status",
            columnDefinition = "INTEGER DEFAULT 0"
    )
    private Integer greeningStatus;

    public BookEvent(){
    }

    public BookEvent(
            String title,
            Double management_amount,
            Integer status,
            String other_information,
            Integer attendees,
            Double total_amount,
            LocalDate timestamp,
            LocalDate completion_timestamp) {

        this.title = title;
        this.management_amount = management_amount;
        this.status = status;
        this.other_information = other_information;
        this.attendees = attendees;
        this.total_amount = total_amount;
        this.timestamp = timestamp;
        this.completion_timestamp = completion_timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getManagement_amount() {
        return management_amount;
    }

    public void setManagement_amount(Double management_amount) {
        this.management_amount = management_amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOther_information() {
        return other_information;
    }

    public void setOther_information(String other_information) {
        this.other_information = other_information;
    }

    public Integer getAttendees() {
        return attendees;
    }

    public void setAttendees(Integer attendees) {
        this.attendees = attendees;
    }

    public Double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Double total_amount) {
        this.total_amount = total_amount;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDate getCompletion_timestamp() {
        return completion_timestamp;
    }

    public void setCompletion_timestamp(LocalDate completion_timestamp) {
        this.completion_timestamp = completion_timestamp;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        user = user;
    }

    public Integer getGreeningStatus() {
        return greeningStatus;
    }

    public void setGreeningStatus(Integer greeningStatus) {
        this.greeningStatus = greeningStatus;
    }
}
