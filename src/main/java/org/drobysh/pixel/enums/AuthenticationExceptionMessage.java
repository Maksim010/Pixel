package org.drobysh.pixel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthenticationExceptionMessage {

    AUTHENTICATION_EXCEPTION_MESSAGE("Вы не прошли аутентификацию");
    private final String message;

}
