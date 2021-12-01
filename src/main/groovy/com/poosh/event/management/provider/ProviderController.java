package com.poosh.event.management.provider;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import com.poosh.event.management.globaldto.StatusDto;
import com.poosh.event.management.provider.dto.ProviderCreateDto;
import com.poosh.event.management.provider.dto.ProviderUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController()
@RequestMapping("api/v1/provider")
@Validated
public class ProviderController {

    private final ProviderService providerService;

    @Autowired
    public ProviderController( ProviderService providerService) {
        this.providerService = providerService;
    }
    @GetMapping
    public BaseApiResponse getAllProviders(){
        return providerService.getAllProviders();
    }

    @GetMapping("/{id}")
    public BaseApiResponse getProviderById(@PathVariable("id") @Min(1L) Long id){
        return providerService.getProviderById(id);
    }

    @PostMapping
    public BaseApiResponse addProvider(@Valid @RequestBody ProviderCreateDto body){
        return providerService.addProvider(body);
    }

    @PutMapping("{id}")
    public BaseApiResponse updateProvider(@Valid @RequestBody ProviderUpdateDto body,
                                                  @PathVariable("id") @Min(1) Long providerId){
        return providerService.updateProvider(providerId, body);
    }

    @PutMapping("status/{id}")
    public BaseApiResponse updateProviderStatus(@Valid @RequestBody StatusDto body,
                                                        @PathVariable("id") @Min(1) Long providerId){
        return providerService.updateStatus(providerId, body.getStatus());
    }



}
