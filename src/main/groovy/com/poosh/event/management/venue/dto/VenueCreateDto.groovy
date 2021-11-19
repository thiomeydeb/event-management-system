package com.poosh.event.management.venue.dto

import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class VenueCreateDto {
    @NotBlank
    @Size(min = 3, max = 20)
    String name

    @NotBlank
    @Size(min = 3, max = 20)
    String location

    @DecimalMin(value = "0")
    Double amount
}
