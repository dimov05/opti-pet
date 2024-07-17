package com.opti_pet.backend_app.exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorModel handleNotFound(RuntimeException ex) {
        return buildExceptionResponse(List.of(ex.getMessage()), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({ConversionFailedException.class, HttpMessageNotReadableException.class, BadRequestException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorModel handleBadRequest(RuntimeException ex) {
        return buildExceptionResponse(List.of(ex.getMessage()), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorModel handleUnauthorized(RuntimeException ex) {
        return buildExceptionResponse(List.of(ex.getMessage()), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorModel handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> messageList = fieldErrors
                .stream()
                .map(fieldError ->
                        String.format("Field '%s' %s.",
                                fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        return buildExceptionResponse(messageList, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorModel handleConstraintViolationException(ConstraintViolationException e) {
        List<String> messages = e.getConstraintViolations()
                .stream()
                .map(ex -> String.format("Field '%s' %s.",
                        ex.getPropertyPath().toString().substring(ex.getPropertyPath().toString().lastIndexOf('.') + 1),
                        ex.getMessage()))
                .toList();

        return buildExceptionResponse(messages, HttpStatus.BAD_REQUEST.value());
    }

    private ErrorModel buildExceptionResponse(List<String> messageList, int statusCode) {
        return ErrorModel.builder()
                .statusCode(statusCode)
                .messages(messageList)
                .build();
    }
}