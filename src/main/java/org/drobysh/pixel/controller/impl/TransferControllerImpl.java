package org.drobysh.pixel.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drobysh.pixel.controller.TransferController;
import org.drobysh.pixel.dto.request.TransferDto;
import org.drobysh.pixel.services.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user-service/transfer")
public class TransferControllerImpl implements TransferController {

    private final TransferService transferService;

    @Override
    @PostMapping
    public ResponseEntity<BigDecimal> transferMoney(
            @RequestAttribute("userId") Long id,
            @RequestBody @Valid TransferDto transferDto) {
        log.info("transferMoney started with id {} and request {}", id, transferDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transferService.transfer(id, transferDto));
    }
}
