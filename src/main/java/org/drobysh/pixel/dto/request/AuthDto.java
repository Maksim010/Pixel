package org.drobysh.pixel.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.drobysh.pixel.utils.validator.EmailOrMobile;

@EmailOrMobile
@Builder
public record AuthDto(

    @Email
    @NotBlank
    String email,

    @Pattern(regexp = "^\\d{11}$", message = "Телефон должен состоять из 11 цифр")
    String phone,

    @Pattern(regexp = "^.{8,500}$", message = "Пароль должен содержать не менее 8 и не более 500 символов")
    String password

) {
}
