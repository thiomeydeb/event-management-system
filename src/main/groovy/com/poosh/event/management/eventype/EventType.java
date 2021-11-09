package com.poosh.event.management.eventype;

import javax.persistence.*;
import java.util.Locale;

@Entity( name = "EventType")
@Table( name = "event_type")

public class EventType {

    @SequenceGenerator(
            name= "event_type_sequence",
            sequenceName = "event_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "event_type_sequence"
    )
    @Id
    @Column(
            name ="id",
            updatable = false,
            nullable = false
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "VARCHAR(200)"
    )
    private String name;

    @Column(
            name = "is_active",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private Boolean isActive;

    public EventType() {
    }

    public EventType(String name) {
        this.name = name;
    }

    public EventType(String name, Boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
