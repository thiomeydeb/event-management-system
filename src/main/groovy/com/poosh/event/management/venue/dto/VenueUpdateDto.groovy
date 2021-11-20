package com.poosh.event.management.venue.dto

import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Size

class VenueUpdateDto {
    @Size(min = 3, max = 20)
    String name

    @Size(min = 3, max = 20)
    String location

    @DecimalMin(value = "0.01")
    BigDecimal amount

    Boolean isActive
}
