package com.poosh.event.management.providers;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends CrudRepository<Provider, Long> {
    void updateProviderStatus(Long id, boolean status);

}
