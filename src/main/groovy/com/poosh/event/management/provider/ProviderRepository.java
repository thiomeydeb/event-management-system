package com.poosh.event.management.provider;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ProviderRepository extends CrudRepository<Provider, Long> {
    @Modifying
    @Query("UPDATE Provider provider SET provider.isActive = ?2 WHERE provider.id = ?1")
    int updateProviderStatus(Long id, boolean status);

    @Modifying
    @Query(
            value = "UPDATE provider SET title = :title, cost = :cost, category_id = :categoryId WHERE id = :providerId",
            nativeQuery = true
    )
    int updateProvider(
            @Param("providerId") Long providerId,
            @Param("title") String title,
            @Param("cost") Double cost,
            @Param("categoryId") Long categoryId
    );

}
