package org.drobysh.pixel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountNotFoundExceptionMessage {

    ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE("Аккаунт не найден");
    private final String message;

}
