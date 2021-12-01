package com.poosh.event.management.eventype.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class EventTypeCreateDto {
    @NotBlank
    @Size(min = 3, max = 20)
    String name

    @NotBlank
    @Size(min = 3, max = 20)
    String location
}
