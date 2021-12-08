package com.poosh.event.management.eventmanagement

import com.sun.istack.NotNull

import javax.validation.constraints.Max
import javax.validation.constraints.Min

class LinkPlannerDto {
    @NotNull
    @Min(1L)
    @Max(Long.MAX_VALUE)
    Long eventId

    @NotNull
    @Min(1L)
    @Max(Long.MAX_VALUE)
    Long userId
}
