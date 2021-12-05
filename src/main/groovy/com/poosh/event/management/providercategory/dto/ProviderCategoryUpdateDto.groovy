package com.poosh.event.management.providercategory.dto

import javax.validation.constraints.Size

class ProviderCategoryUpdateDto {
    @Size(min = 3, max = 20)
    String name
    @Size(min = 1, max = 20)
    String code
    Boolean isActive
}
