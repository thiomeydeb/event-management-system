package com.poosh.event.management.providers;


import javax.persistence.*;

@Entity(name = "Provider")
@Table(name = "Provider")
public class Provider {

    @SequenceGenerator(
            name = "provider_sequence",
            sequenceName = "provider_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "provider_sequence"
    )

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Integer id;

    @Column(
            name = "title",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private String title;

    @Column(
            name = "category_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Integer category_id;

    @Column(
            name = "cost",
            nullable = false,
            columnDefinition = "DOUBLE PRECISION"
    )
    private Double cost;

    @Column(
            name ="is_active",
            updatable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private Boolean isActive;

    public Provider(){

    }
    public Provider(String title, Integer category_id, Double cost, Boolean isActive) {
        this.title = title;
        this.category_id = category_id;
        this.cost = cost;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
