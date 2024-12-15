package com.example.security.dto

data class UserRolesDto(
    val username: String?,
    val roles: MutableSet<String> = mutableSetOf()
)