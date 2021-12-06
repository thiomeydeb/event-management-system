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
            updatable = false,
            columnDefinition = "BIGSERIAL"
    )
    private Long id;

    @Column(
            name = "event_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Long eventId;

    @Column(
            name = "venue_id",
            columnDefinition = "BIGINT"
    )
    private Long venueId;

    @Column(
            name = "provider_id",
            columnDefinition = "BIGINT"
    )
    private Long providerId;

    @Column(
            name = "planner_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Long plannerId;

    @Column(
            name = "status",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private Boolean status;

    public PlannedEventDetails(){

    }

    public PlannedEventDetails(Long eventId, Long venueId, Long providerId, Long plannerId, Boolean status) {
        this.eventId = eventId;
        this.venueId = venueId;
        this.providerId = providerId;
        this.plannerId = plannerId;
        this.status = status;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Long getPlannerId() {
        return plannerId;
    }

    public void setPlannerId(Long plannerId) {
        this.plannerId = plannerId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
