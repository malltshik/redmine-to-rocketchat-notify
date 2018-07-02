package ru.tecforce.worktime.web

import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.tecforce.worktime.exceptions.ConflictException
import ru.tecforce.worktime.exceptions.NotFoundException

@ControllerAdvice
class ControllerAdviceHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ResponseException> {
        return ResponseEntity(ResponseException(ex.message ?: INTERNAL_SERVER_ERROR.reasonPhrase,
            ex.javaClass.name, INTERNAL_SERVER_ERROR.value()), INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(ConflictException::class)
    fun handleException(ex: ConflictException): ResponseEntity<ResponseException> {
        return ResponseEntity(ResponseException(ex.message, ex.javaClass.name, CONFLICT.value()), CONFLICT)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleException(ex: NotFoundException): ResponseEntity<ResponseException> {
        return ResponseEntity(ResponseException(ex.message, ex.javaClass.name, NOT_FOUND.value()), NOT_FOUND)
    }

    data class ResponseException(val message: String, val type: String, val status: Int)
}