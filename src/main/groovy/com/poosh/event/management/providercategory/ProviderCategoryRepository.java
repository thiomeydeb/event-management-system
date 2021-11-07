package com.poosh.event.management.providercategory;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderCategoryRepository extends CrudRepository<ProviderCategory, Long> {
    @Modifying
    @Query("UPDATE ProviderCategory category SET category.isActive = ?2 WHERE category.id = ?1")
    void updateProviderCategoryStatus(Long id, boolean status);
}
