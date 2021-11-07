package com.poosh.event.management.providercategory;

import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/provider-category")
public class ProviderCategoryController {

    private final ProviderCategoryService providerCategoryService;

    @Autowired
    public ProviderCategoryController(ProviderCategoryService providerCategoryService) {
        this.providerCategoryService = providerCategoryService;
    }

    @GetMapping
    public void getAllProviderCategory(){
        providerCategoryService.getAllProviderCategory();
    }


    @GetMapping("/{id}")
    public Object getProviderCategoryById(@PathVariable("id") Long id){
        return providerCategoryService.getProviderCategoryById(id);
    }

    @PostMapping
    public void addProviderCategory(@RequestBody ProviderCategory body){
        providerCategoryService.addProviderCategory(body);
    }

}
