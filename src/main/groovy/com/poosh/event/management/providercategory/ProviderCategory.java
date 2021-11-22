package com.poosh.event.management.providercategory;

import com.poosh.event.management.provider.Provider;

import javax.persistence.*;
import java.util.List;

@Entity(name = "ProviderCategory")
@Table(name = "provider_category")
public class ProviderCategory {

    @SequenceGenerator(
            name = "provider_category_sequence",
            sequenceName = "provider_category_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "provider_category_sequence"
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
            columnDefinition = "VARCHAR(50)"
    )
    private String name;

    @Column(
            name = "is_active",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT TRUE"
    )
    private Boolean isActive;

    @OneToMany(
            mappedBy = "providerCategory",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private List<Provider> providerList;

    public ProviderCategory(){

    }

    public ProviderCategory(String name) {
        this.name = name;
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
