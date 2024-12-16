package com.example.security.controller

import com.example.security.dto.UserWithTokenDto
import com.example.security.dto.UserDto
import com.example.security.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/change_credentials")
    fun changeCredentials(
        @RequestBody userDto: UserDto,
        request: HttpServletRequest
    ): UserWithTokenDto {
        // Extract the JWT token from the Authorization header
        val authorizationHeader = request.getHeader("Authorization")
        val token = if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader.substring(7) // Remove "Bearer " prefix
        } else {
            throw IllegalArgumentException("Missing or invalid Authorization header")
        }

        // Pass the token to the service
        return userService.changeCredentials(userDto, token)
    }
}