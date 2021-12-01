package com.poosh.event.management.user.dto

import javax.validation.constraints.Email
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class UserUpdateDto {
    @Size(min = 3, max = 50)
    String firstName

    @Size(min = 3, max = 50)
    String middleName

    @Size(min = 3, max = 50)
    String lastName

    @Size(min = 3, max = 255)
    String password

    @Email
    String email

    @Size(min = 3, max = 13)
    String phoneNumber

    @Size(min = 3, max = 200)
    String identificationType

    @Size(min = 3, max = 200)
    String identificationNumber

    @Size(min = 3, max = 200)
    String companyName

    @Min(1L)
    Long userId
}
