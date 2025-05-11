package org.drobysh.pixel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailBadRequestExceptionMessage {

    ONLY_ONE_EMAIL("Вы не можете удалить email"),
    EMAIL_ALREADY_EXISTS("Такой email уже существует");
    private final String message;
}
