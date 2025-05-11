package org.drobysh.pixel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PhoneBadRequestExceptionMessage {

    ONLY_ONE_PHONE("Вы не можете удалить телефон"),
    PHONE_ALREADY_EXISTS("Такой телефон уже существует");
    private final String message;
}
