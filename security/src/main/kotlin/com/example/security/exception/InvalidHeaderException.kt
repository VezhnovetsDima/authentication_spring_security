package com.example.security.exception

class InvalidHeaderException(message: String? = "Missing or invalid Authorization header") : IllegalArgumentException(message) {
}