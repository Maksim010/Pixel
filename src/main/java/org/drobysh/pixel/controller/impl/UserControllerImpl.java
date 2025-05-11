package org.drobysh.pixel.controller.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.drobysh.pixel.controller.UserController;
import org.drobysh.pixel.dto.UserSearchCriteria;
import org.drobysh.pixel.dto.request.EmailDto;
import org.drobysh.pixel.dto.request.PhoneDto;
import org.drobysh.pixel.dto.response.UserDto;
import org.drobysh.pixel.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user-service")
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    @DeleteMapping("/user/email/{email}")
    public ResponseEntity<String> removeEmail(
            @RequestAttribute("userId") Long userId,
            @PathVariable String email) {
        log.info("removeEmail is started with email {}, id{}", email, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.deleteEmail(userId,email));
    }

    @Override
    @PatchMapping("/user/email")
    public ResponseEntity<String> updateEmail(
            @RequestAttribute("userId") Long userId,
            @RequestBody @Valid EmailDto request) {
        log.info("updateEmail is started with request {}", request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateEmail(userId,request));
    }

    @Override
    @PostMapping("/user/new-email/{email}")
    public ResponseEntity<String> addEmail(
            @RequestAttribute("userId") Long userId,
            @PathVariable String email) {
        log.info("addEmail is started with email {} and userId {}", email, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.createEmail(userId,email));
    }

    @Override
    @DeleteMapping("/user/phone/{phone}")
    public ResponseEntity<String> removePhone(
            @RequestAttribute("userId") Long userId,
            @PathVariable String phone) {
        log.info("removePhone is started with userId {} and phone {}", userId, phone);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.deletePhoneNumber(userId,phone));
    }

    @Override
    @PatchMapping("/user/phone")
    public ResponseEntity<String> updatePhone(
            @RequestAttribute("userId") Long userId,
            @RequestBody @Valid PhoneDto request) {
        log.info("updatePhone is started with userId {} and request{}", userId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updatePhone(userId,request));
    }

    @Override
    @PostMapping("/user/new-phone/{phone}")
    public ResponseEntity<String> addPhone(
            @RequestAttribute("userId") Long userId,
            @PathVariable
            @Pattern(regexp = "^\\d{11}$", message = "Телефон должен состоять из 11 цифр")
            String phone) {
        log.info("addPhone is started with id {} and phone {}", userId, phone);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.createPhone(userId,phone));
    }

    @Override
    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>> getUsers(
            @RequestParam(name = "size", defaultValue = "1") Integer size,
            @RequestParam(name = "offset" , defaultValue = "0") Integer offset,
            @RequestParam(name = "dateOfBirth", required = false) LocalDate dateOfBirth,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email) {
        log.info("getUsers is started");

        UserSearchCriteria criteria = UserSearchCriteria.builder()
                .dateOfBirth(dateOfBirth)
                .phone(phone)
                .name(name)
                .email(email)
                .build();

        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(userService.getUsers(criteria,offset,size));
    }

}
