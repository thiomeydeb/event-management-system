package com.poosh.event.management.user.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class UserCreateDto {
    @NotBlank
    @Size(min = 3, max = 50)
    String firstName

    @Size(min = 3, max = 50)
    String middleName

    @NotBlank
    @Size(min = 3, max = 50)
    String lastName

    @NotBlank
    @Size(min = 3, max = 255)
    String password

    @NotBlank
    @Size(min = 3, max = 255)
    String confirmPassword

    @Email
    String email

    @NotBlank
    @Size(min = 3, max = 13)
    String phoneNumber

    @NotBlank
    @Size(min = 3, max = 200)
    String identificationType

    @NotBlank
    @Size(min = 3, max = 200)
    String identificationNumber

    @NotBlank
    @Size(min = 3, max = 200)
    String companyName
}
