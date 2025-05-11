package org.drobysh.pixel.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drobysh.pixel.controller.AuthController;
import org.drobysh.pixel.dto.request.AuthDto;
import org.drobysh.pixel.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user-service/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<String> auth(@RequestBody @Valid AuthDto authDto) {
        log.info("auth is started: {}", authDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.authenticate(authDto));
    }
}
