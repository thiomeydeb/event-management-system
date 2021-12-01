package com.poosh.event.management.providercategory.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class ProviderCategoryCreateDto {
    @NotBlank
    @Size(min = 3, max = 20)
    String name
}
