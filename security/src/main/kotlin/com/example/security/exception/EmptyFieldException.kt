package com.example.security.exception

class EmptyFieldException(message: String = "Field can't be null") : IllegalArgumentException(message) {
}