package com.example.security.service

import com.example.security.dto.AuthenticationRole
import com.example.security.dto.UserRolesDto
import com.example.security.entity.RoleEntity
import com.example.security.entity.UserEntity
import com.example.security.exception.EmptyFieldException
import com.example.security.exception.RoleNotExistsException
import com.example.security.exception.UserNotExistsException
import com.example.security.repository.RoleRepository
import com.example.security.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) {
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    fun authenticateAndSetRole(auth: AuthenticationRole): UserRolesDto {
        val (user, role) = pair(auth)
        user.roles.add(role)

        userRepository.saveAndFlush(user)

        return UserRolesDto(
            auth.username,
            user.roles.map{ it.name!! }.toMutableSet()
        )
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    fun removeRole(auth: AuthenticationRole): UserRolesDto {
        val (user, role) = pair(auth)

        user.roles.remove(role)

        userRepository.saveAndFlush(user)

        return UserRolesDto(
            auth.username,
            user.roles.map{ it.name!! }.toMutableSet()
        )
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private fun pair(auth: AuthenticationRole): Pair<UserEntity, RoleEntity> {
        val user = userRepository.findByEmail(auth.username ?: throw EmptyFieldException())
            ?: throw UserNotExistsException("User with this email not exists AdminService.pair()")

        val role = roleRepository.findByName(auth.setRole ?: throw EmptyFieldException())
            ?: throw RoleNotExistsException("Role not exists  AdminService.pair()")
        return Pair(user, role)
    }
}