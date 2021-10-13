package com.poosh.event.management.providercategory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderCategoryRepository extends CrudRepository<ProviderCategory, Long> {
    void updateProviderCategoryStatus(Long id, boolean status);
}
