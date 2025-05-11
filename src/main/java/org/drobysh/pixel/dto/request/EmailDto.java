package org.drobysh.pixel.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record EmailDto(

    @Email
    @NotBlank
    String email,

    @Email
    @NotBlank
    String newEmail
){
}




