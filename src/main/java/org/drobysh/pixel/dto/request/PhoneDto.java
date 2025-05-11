package org.drobysh.pixel.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record PhoneDto(

    @Pattern(regexp = "^\\d{11}$", message = "Телефон должен состоять из 11 цифр")
    String phone,

    @Pattern(regexp = "^\\d{11}$", message = "Телефон должен состоять из 11 цифр")
    String newPhone
){
}




