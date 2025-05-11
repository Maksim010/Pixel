package org.drobysh.pixel.exceptions.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.drobysh.pixel.dto.ErrorDto;
import org.drobysh.pixel.exceptions.BadRequestException;
import org.drobysh.pixel.exceptions.ForbiddenException;
import org.drobysh.pixel.exceptions.InvalidJwtException;
import org.drobysh.pixel.exceptions.NotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            BadRequestException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            IllegalArgumentException.class,
            InvalidJwtException.class,
            EmptyResultDataAccessException.class,})
    public ErrorDto handleBadRequestException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return getErrorDto(HttpStatus.BAD_REQUEST,exception);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ErrorDto handleNotFoundException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return getErrorDto(HttpStatus.NOT_FOUND,exception);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({ForbiddenException.class})
    public ErrorDto handleForbiddenException(Exception exception) {
        log.error("handleForbiddenException - exception = {}", exception.getMessage());
        return getErrorDto(HttpStatus.FORBIDDEN,exception);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ErrorDto handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR,exception);
    }

    private ErrorDto getErrorDto(HttpStatus httpStatus, Exception exception) {
        return ErrorDto.builder()
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(exception.getMessage())
                .timestamp(Instant.now())
                .build();
    }
}

