package com.example.security.controller.controllerAdvice

import com.example.security.dto.ExceptionDto
import com.example.security.exception.EmptyFieldException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
class UnionControllerAdvice { //refactor in future

    private val logger: Logger = LoggerFactory.getLogger(UnionControllerAdvice::class.java)

    @ExceptionHandler(Exception::class)
    fun handleEmptyFieldException(ex: EmptyFieldException): ExceptionDto {
        logger.error("Exception occured ${ex.javaClass} \\n message: ${ex.message}" )
        return ExceptionDto(ex.message)
    }
}