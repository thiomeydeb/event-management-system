package com.poosh.event.management.venue;

import javax.persistence.*;

@Entity(name = "Venue")
@Table(name = "venue")

public class Venue {

    @SequenceGenerator(
            name= "venue_sequence",
            sequenceName = "venue_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "venue_sequence"
    )
    @Id
    @Column(
            name ="id",
            updatable = false,
            nullable = false
    )
    private Long id;

    @Column(
            name ="name",
            nullable = false,
            columnDefinition = "VARCHAR(50)"
    )
    private String name;

    @Column(
            name ="location",
            columnDefinition = "VARCHAR(100)"
    )
    private String location;

    @Column(
            name ="is_active",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private Boolean isActive;

    @Column(
            name ="amount",
            nullable = false,
            columnDefinition = "DOUBLE PRECISION"
    )
    private Double amount;

    public Venue(){

    }
    public Venue(
            String name,
            String location,
            Boolean isActive,
            Double amount) {
        this.name = name;
        this.location = location;
        this.isActive = isActive;
        this.amount = amount;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
