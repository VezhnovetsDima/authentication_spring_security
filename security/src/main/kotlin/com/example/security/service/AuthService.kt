package com.example.security.service

import com.example.security.dto.UserWithTokenDto
import com.example.security.dto.AuthenticationCredentials
import com.example.security.dto.UserDto
import com.example.security.mapper.UserMapper
import com.example.security.repository.RoleRepository
import com.example.security.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Service
class AuthService(
    private val repository: UserRepository,
    private val mapper: UserMapper,
    private val roleRepository: RoleRepository,
    private val jwtService: JwtService,
) {
    private val SECRET_KEY = "your-secret-key!"
    private val INIT_VECTOR = "your-init-vector"

    @Transactional
    fun register(user: UserDto): UserWithTokenDto {
        val userEntity = mapper.toEntity(user)
        //check if user not exists
        if (repository.findByEmail(user.email) != null) throw RuntimeException("User exists")

        userEntity.encryptedPassword = encrypt(user.password)

        userEntity.roles = mutableSetOf(roleRepository.findByName("ROLE_USER")!!)

        repository.saveAndFlush(userEntity)

        val token = jwtService.generateToken(mapOf(), repository.saveAndFlush(userEntity))

        return UserWithTokenDto(mapper.toDto(userEntity), token)
    }

    fun authenticate(auth: AuthenticationCredentials): UserWithTokenDto {
        val user = repository.findByEmail(auth.username ?: throw IllegalArgumentException(""))
            ?: throw IllegalArgumentException("")

        if (decrypt(user.encryptedPassword) == auth.password) {
            val token = jwtService.generateToken(
                mapOf(),
                user
            )

            return UserWithTokenDto(mapper.toDto(user), token)
        } else {
            throw RuntimeException("wrong password")
        }
    }

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        val keySpec: SecretKey = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
        val ivSpec = IvParameterSpec(INIT_VECTOR.toByteArray())

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        val encrypted = cipher.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    private fun decrypt(password: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        val keySpec: SecretKey = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
        val ivSpec = IvParameterSpec(INIT_VECTOR.toByteArray())

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        val original = cipher.doFinal(Base64.getDecoder().decode(password))
        return String(original)
    }
}