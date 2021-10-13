package com.poosh.event.management.venues;

import javax.persistence.*;

@Entity(name = "Venues")
@Table(name = "venues")

public class Venues {

    @SequenceGenerator(
            name= "venues_sequence",
            sequenceName = "venues_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "venues_sequence"
    )
    @Id
    @Column(
            name ="id",
            updatable = false,
            nullable = false
    )
    private Integer id;

    @Column(
            name ="name",
            updatable = false,
            columnDefinition = "VARCHAR(50)"
    )
    private String name;

    @Column(
            name ="location",
            updatable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private String location;

    @Column(
            name ="is_active",
            updatable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private Boolean isActive;

    @Column(
            name ="amount",
            updatable = false,
            columnDefinition = "DOUBLE PRECISION"
    )
    private Double amount;

    public Venues(){

    }
    public Venues (
            String name,
            String location,
            Boolean isActive,
            Double amount) {
        this.name = name;
        this.location = location;
        this.isActive = isActive;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
