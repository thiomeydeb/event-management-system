package com.poosh.event.management.providercategory

import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProviderCategoryService {

    private final ProviderCategoryRepository providerCategoryRepository

    @Autowired //inject the object dependency implicitly
    public ProviderCategoryService(ProviderCategoryRepository providerCategoryRepository) {
        this.providerCategoryRepository = providerCategoryRepository
    }

    void addProviderCategory (ProviderCategory providerCategory){
        providerCategoryRepository.save(providerCategory)
    }

    void updateProviderCategory(ProviderCategory providerCategory){
        providerCategoryRepository.save(providerCategory)
    }

    void updateStatus(Long id, boolean status){
        providerCategoryRepository.updateProviderCategoryStatus(id, status)
    }

    void getAllProviderCategory(){
        providerCategoryRepository.findAll()
    }

    def getProviderCategoryById(Long id){
        def event = providerCategoryRepository.findById(id)
        event;
    }
}
