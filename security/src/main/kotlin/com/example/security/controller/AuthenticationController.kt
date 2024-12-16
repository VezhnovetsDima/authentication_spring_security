package com.example.security.controller

import com.example.security.dto.UserWithTokenDto
import com.example.security.dto.AuthenticationCredentials
import com.example.security.dto.UserDto
import com.example.security.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(@RequestBody user: UserDto): UserWithTokenDto {
        return authService.register(user)
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody auth: AuthenticationCredentials): UserWithTokenDto {
        return authService.authenticate(auth)
    }
}