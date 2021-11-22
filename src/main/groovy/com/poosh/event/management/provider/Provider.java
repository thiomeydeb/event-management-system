package com.poosh.event.management.provider;


import com.poosh.event.management.providercategory.ProviderCategory;

import javax.persistence.*;

@Entity(name = "Provider")
@Table(name = "provider")
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
    private Long id;

    @Column(
            name = "title",
            nullable = false,
            columnDefinition = "VARCHAR(100)"
    )
    private String title;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(
            name = "category_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey( name = "provider_category_provider_fk")
    )
    private ProviderCategory providerCategory;

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
    public Provider(String title, Double cost, Boolean isActive, ProviderCategory providerCategory) {
        this.title = title;
        this.cost = cost;
        this.isActive = isActive;
        this.providerCategory = providerCategory;
    }

    public Provider(Long id, String title, Double cost, Boolean isActive, ProviderCategory providerCategory) {
        this.id = id;
        this.title = title;
        this.cost = cost;
        this.isActive = isActive;
        this.providerCategory = providerCategory;
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

    public ProviderCategory getProviderCategory() {
        return providerCategory;
    }

    public void setProviderCategory(ProviderCategory providerCategory) {
        this.providerCategory = providerCategory;
    }
}
