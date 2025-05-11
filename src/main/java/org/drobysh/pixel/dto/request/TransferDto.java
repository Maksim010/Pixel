package org.drobysh.pixel.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransferDto(

        @NotNull(message = "Id получателя обязательно")
        Long id,

        @NotNull(message = "Сумма перевода обязательна")
        @Positive(message = "Сумма должна быть положительной")
        @Digits(integer = 10, fraction = 2, message = "Некорректный формат суммы")
        BigDecimal amount
) {
}
