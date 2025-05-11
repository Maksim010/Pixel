package org.drobysh.pixel.utils.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.drobysh.pixel.utils.validator.impl.EmailOrMobileValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailOrMobileValidator.class)
public @interface EmailOrMobile {

    String message() default "Email или телефон должны быть переданы";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
