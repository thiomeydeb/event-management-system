package com.poosh.event.management.eventype.dto

import javax.validation.constraints.Size

class EventTypeUpdateDto {
    @Size(min = 3, max = 20)
    String name
    Boolean isActive
}
