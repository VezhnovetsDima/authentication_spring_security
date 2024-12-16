package com.example.security.dto

data class UserWithTokenDto (
    var user: UserDto?,
    var token: String?
)