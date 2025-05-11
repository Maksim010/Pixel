package org.drobysh.pixel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PasswordBadRequestExceptionMessage {

    WRONG_PASSWORD("Неверный пароль");
    private final String message;
}
