package org.drobysh.pixel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserNotFoundExceptionMessage {

    USER_NOT_FOUND("Пользователь не найден");
    private final String message;

}
