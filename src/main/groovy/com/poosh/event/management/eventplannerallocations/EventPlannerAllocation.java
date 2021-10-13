package com.poosh.event.management.eventplannerallocations;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "EventPlannerAllocation")
@Table(name = "event_planner_allocation")
public class EventPlannerAllocation {

    @SequenceGenerator(
            name = "event_planner_allocation_sequence",
            sequenceName = "event_planner_allocation_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "event_planner_allocation_sequence"
    )

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Integer id;

    @Column(
            name = "event_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Integer event_id;

    @Column(
            name = "user_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Integer user_id;

    @Column(
            name = "status",
            nullable = false,
            columnDefinition = "BOOLEAN"
    )
    private Boolean status;

    @Column(
            name ="time_allocation",
            nullable = false,
            columnDefinition = "TIMESTAMP(6) WITH TIME ZONE"
    )
    private LocalDate time_allocated;

    public EventPlannerAllocation() {

    }
    public EventPlannerAllocation(
            Integer event_id,
            Integer user_id,
            Boolean status,
            LocalDate time_allocated) {
        this.event_id = event_id;
        this.user_id = user_id;
        this.status = status;
        this.time_allocated = time_allocated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Integer event_id) {
        this.event_id = event_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDate getTime_allocated() {
        return time_allocated;
    }

    public void setTime_allocated(LocalDate time_allocated) {
        this.time_allocated = time_allocated;
    }
}
