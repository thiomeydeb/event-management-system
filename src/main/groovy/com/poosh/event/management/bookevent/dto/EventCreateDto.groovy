package com.poosh.event.management.bookevent.dto

import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class EventCreateDto {
    @Min(1L)
    Long clientId

    @NotBlank
    @Size(min = 3, max = 100)
    String name

    @NotNull
    @Min(1L)
    Long eventTypeId

    @NotNull
    @Min(1L)
    Integer attendees

    @NotNull
    @DecimalMin(value = "0")
    Double managementAmount

    @NotNull
    @DecimalMin(value = "0")
    Double totalAmount

    @NotBlank
    @Size(min = 3, max = 500)
    String otherInformation

    @NotNull
    @Min(1L)
    Long venueId

    @NotNull
    @Min(1L)
    Long entertainmentId

    @NotNull
    @Min(1L)
    Long cateringId

    @NotNull
    @Min(1L)
    Long securityId

    @NotNull
    @Min(1L)
    Long designId

    @NotNull
    @Min(1L)
    Long mcId

}
