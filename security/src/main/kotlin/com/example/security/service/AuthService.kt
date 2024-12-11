package com.example.security.service

import com.example.security.dto.AuthResponseDto
import com.example.security.dto.AuthenticationCredentials
import com.example.security.dto.UserDto
import com.example.security.mapper.UserMapper
import com.example.security.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val repository: UserRepository,
    private val mapper: UserMapper,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {
    fun register(user: UserDto): AuthResponseDto {
        val token = jwtService.generateToken(mapOf(), repository.saveAndFlush(mapper.toEntity(user)))

        return AuthResponseDto(token)
    }

    fun authenticate(auth: AuthenticationCredentials): AuthResponseDto {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                auth.username,
                auth.password
            )
        )

        val token = jwtService.generateToken(
            mapOf(),
            repository.findByEmail(auth.username ?: throw IllegalArgumentException(""))
                ?: throw IllegalArgumentException("")
        )

        return AuthResponseDto(token)
    }
}