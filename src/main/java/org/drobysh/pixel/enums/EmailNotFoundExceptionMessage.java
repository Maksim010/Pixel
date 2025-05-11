package org.drobysh.pixel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailNotFoundExceptionMessage {

    EMAIL_NOT_FOUND("Такого email нет");
    private final String message;
}
