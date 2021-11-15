package com.poosh.event.management.providercategory;

import com.poosh.event.management.apiresponse.BaseApiResponse;
import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import com.poosh.event.management.globaldto.StatusDto;
import com.poosh.event.management.providercategory.dto.ProviderCategoryCreateDto;
import com.poosh.event.management.providercategory.dto.ProviderCategoryUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("api/v1/provider-category")
@Validated
public class ProviderCategoryController {

    private final ProviderCategoryService providerCategoryService;

    @Autowired
    public ProviderCategoryController(ProviderCategoryService providerCategoryService) {
        this.providerCategoryService = providerCategoryService;
    }

    @GetMapping
    public BaseApiResponse getAllProviderCategory(){
        return providerCategoryService.getAllProviderCategory();
    }


    @GetMapping("/{id}")
    public BaseApiResponse getProviderCategoryById(@PathVariable("id") @Min(1) Long id){
        return providerCategoryService.getProviderCategoryById(id);
    }

    @PostMapping
    public BaseApiResponse addProviderCategory(@RequestBody ProviderCategoryCreateDto body){
        return providerCategoryService.addProviderCategory(body);
    }

    @PutMapping("{id}")
    public BaseApiResponse updateProviderCategory(@Valid @RequestBody ProviderCategoryUpdateDto body,
                                                  @PathVariable("id") @Min(1) Long providerCategoryId){
        return providerCategoryService.updateProviderCategory(providerCategoryId, body);
    }

    @PutMapping("status/{id}")
    public BaseApiResponse updateProviderCategoryStatus(@Valid @RequestBody StatusDto body,
                                                  @PathVariable("id") @Min(1) Long providerCategoryId){
        return providerCategoryService.updateStatus(providerCategoryId, body.getStatus());
    }

}
