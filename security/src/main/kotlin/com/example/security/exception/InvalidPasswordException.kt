package com.example.security.exception

class InvalidPasswordException(message: String = "Wrong password") : IllegalArgumentException(message)  {
}