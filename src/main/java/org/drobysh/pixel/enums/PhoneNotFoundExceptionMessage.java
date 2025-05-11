package org.drobysh.pixel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PhoneNotFoundExceptionMessage {
    PHONE_NOT_FOUND("Такого телефона нет");
    private final String message;
}
