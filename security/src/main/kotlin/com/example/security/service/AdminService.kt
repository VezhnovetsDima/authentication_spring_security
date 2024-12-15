package com.example.security.service

import com.example.security.dto.AuthenticationRole
import com.example.security.dto.UserRolesDto
import com.example.security.entity.RoleEntity
import com.example.security.entity.UserEntity
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
        val user = userRepository.findByEmail(auth.username ?: throw IllegalArgumentException("field can't be null"))
            ?: throw IllegalArgumentException("User with this email not exists")

        val role = roleRepository.findByName(auth.setRole ?: throw IllegalArgumentException("field can't be null"))
            ?: throw IllegalArgumentException("this role not exists")
        return Pair(user, role)
    }
}