package com.poosh.event.management.providercategory;

import javax.persistence.*;

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
    private Integer id;

    @Column(
            name = "title",
            nullable = false,
            columnDefinition = "VARCHAR(50)"
    )
    private String name;

    public ProviderCategory(){

    }

    public ProviderCategory(String name) {
        this.name = name;
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
}
