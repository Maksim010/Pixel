package org.drobysh.pixel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransferExceptionMessage {
    AMOUNT_FROM_EXCEPTION("Недостаточно средств для перевода такой суммы"),
    WRONG_ID("Вы не можете переслать деньги сами себе");
    private final String message;
}
