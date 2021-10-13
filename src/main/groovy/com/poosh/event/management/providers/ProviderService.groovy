package com.poosh.event.management.providers

import com.poosh.event.management.eventype.EventType
import com.poosh.event.management.eventype.EventTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProviderService {

    private final ProviderRepository providerRepository

    @Autowired
    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository
    }

    void addProvider (Provider provider){
       providerRepository.save(provider)
    }

    void updateProvider (Provider provider){
        providerRepository.save(provider)
    }

    void updateStatus(Long id, boolean status){
        providerRepository.updateProviderStatus(id, status)
    }

    void getAllProviders(){
        providerRepository.findAll()
    }


    def Object getProviderById(long id) {
        def provider = providerRepository.findById(id)
        provider;

    }
}
