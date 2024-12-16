package com.example.security.service;

import com.example.security.dto.UserWithTokenDto
import com.example.security.dto.UserDto
import com.example.security.exception.UserNotExistsException
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
    fun changeCredentials(user: UserDto, token: String): UserWithTokenDto {
        val userEmail: String = jwtService.extractUsername(token)

        val userEntity = repository.findByEmail(userEmail)
            ?: throw UserNotExistsException("User with this email not existsUserService.changeCredentials()")

        val newUserEntity = userMapper.toEntity(user)

        newUserEntity.id = userEntity.id

        repository.saveAndFlush(newUserEntity)

        val resultDto = userMapper.toDto(newUserEntity)

        return UserWithTokenDto(resultDto, jwtService.generateToken(mutableMapOf(), newUserEntity))
    }
}
