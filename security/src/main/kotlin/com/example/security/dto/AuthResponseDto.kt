package com.example.security.dto

data class AuthResponseDto (
    var user: UserDto?,
    var token: String?
)