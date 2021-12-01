package com.poosh.event.management.providercategory.dto

import javax.validation.constraints.Size

class ProviderCategoryUpdateDto {
    @Size(min = 3, max = 20)
    String name
    Boolean isActive
}
