package com.poosh.event.management.providers;

import com.poosh.event.management.eventype.EventType;
import com.poosh.event.management.eventype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/v1/provider")
public class ProviderController {

    private final ProviderService providerService;

    @Autowired
    public ProviderController( ProviderService providerService) {
        this.providerService = providerService;
    }
    @GetMapping
    public void getAllProviders(){
        providerService.getAllProviders();
    }

    @GetMapping("/{id}")
    public Object getEventById(@PathVariable("id") Long id){
        return providerService.getProviderById(id);
    }

    @PostMapping
    public void addProvider(@RequestBody Provider body){ providerService.addProvider(body);
    }



}
