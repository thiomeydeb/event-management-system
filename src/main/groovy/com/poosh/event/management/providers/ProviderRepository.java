package com.poosh.event.management.providers;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends CrudRepository<Provider, Long> {
    @Modifying
    @Query("UPDATE Provider provider SET provider.isActive = ?2 WHERE provider.id = ?1")
    void updateProviderStatus(Long id, boolean status);

}
