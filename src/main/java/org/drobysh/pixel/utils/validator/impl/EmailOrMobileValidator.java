package org.drobysh.pixel.utils.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.drobysh.pixel.dto.request.AuthDto;
import org.drobysh.pixel.utils.validator.EmailOrMobile;

public class EmailOrMobileValidator implements ConstraintValidator<EmailOrMobile, AuthDto> {

    /**
     * Валидатор для тела запроса
     * @param request тело запроса
     *
     * @return true, если заполнено только 1 из полей
     */
    @Override
    public boolean isValid(AuthDto request, ConstraintValidatorContext context) {

    boolean isEmailPresent = request.email() != null;

    boolean isMobilePresent = request.phone() != null;

    return (isEmailPresent && !isMobilePresent) || (!isEmailPresent && isMobilePresent);
    }
}
