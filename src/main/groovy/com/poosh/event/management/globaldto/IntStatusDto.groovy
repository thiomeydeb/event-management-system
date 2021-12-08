package com.poosh.event.management.globaldto

import javax.validation.constraints.Max
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

class IntStatusDto {
    @NotNull
    @Min(0L)
    @Max(4L)
    Integer status
}
