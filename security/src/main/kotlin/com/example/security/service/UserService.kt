package com.example.security.service;

import com.example.security.dto.UserDto
import com.example.security.mapper.UserMapper
import com.example.security.repository.UserRepository
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
class UserService(
    private val jwtService: JwtService,
    private val repository: UserRepository,
    private val userMapper: UserMapper
) {

    @Transactional
    fun changeCredentials(user: UserDto, token: String): UserDto {
        val userEmail: String = jwtService.extractUsername(token)

        val userEntity = repository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("User with this email not exists")

        val newUserEntity = userMapper.toEntity(user)

        newUserEntity.id = userEntity.id

        repository.saveAndFlush(newUserEntity)

        val resultDto = userMapper.toDto(newUserEntity)

        resultDto.token = jwtService.generateToken(mutableMapOf(), newUserEntity)

        return resultDto
    }
}