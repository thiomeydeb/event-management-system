package com.poosh.event.management.provider.dto

import com.poosh.event.management.providercategory.ProviderCategory

import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class ProviderUpdateDto {
    @Size(min = 3, max = 200)
    String title
    @Min(1L)
    Long categoryId
    @DecimalMin(value = "0")
    Double cost
    ProviderCategory providerCategory
}
