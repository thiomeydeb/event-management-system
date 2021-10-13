package com.poosh.event.management.plannedeventdetails;

import javax.persistence.*;

@Entity(name = "PlannedEventDetails")
@Table(name = "planned_event_details")
public class PlannedEventDetails {

    @SequenceGenerator(
            name = "planned_event_details_sequence",
            sequenceName = "planned_event_details_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "planned_event_details_sequence"
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
            name = "venue_id",
            nullable = false,
            columnDefinition = "INTEGER"
    )
    private Integer venue_id;

    @Column(
            name = "provider_id",
            nullable = false,
            columnDefinition = "INTEGER"
    )
    private Integer provider_id;

    @Column(
            name = "planner_id",
            nullable = false,
            columnDefinition = "INTEGER"
    )
    private Integer planner_id;

    @Column(
            name = "status",
            nullable = false,
            columnDefinition = "BOOLEAN"
    )
    private Boolean status;

    public PlannedEventDetails(){

    }
    public PlannedEventDetails(
            Integer event_id,
            Integer venue_id,
            Integer provider_id,
            Integer planner_id,
            Boolean status) {
        this.event_id = event_id;
        this.venue_id = venue_id;
        this.provider_id = provider_id;
        this.planner_id = planner_id;
        this.status = status;
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

    public Integer getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(Integer venue_id) {
        this.venue_id = venue_id;
    }

    public Integer getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(Integer provider_id) {
        this.provider_id = provider_id;
    }

    public Integer getPlanner_id() {
        return planner_id;
    }

    public void setPlanner_id(Integer planner_id) {
        this.planner_id = planner_id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
