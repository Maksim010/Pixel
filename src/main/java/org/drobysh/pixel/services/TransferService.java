package org.drobysh.pixel.services;

import org.drobysh.pixel.dto.request.TransferDto;

import java.math.BigDecimal;

public interface TransferService {

    BigDecimal transfer(Long from, TransferDto transferDto);
}
