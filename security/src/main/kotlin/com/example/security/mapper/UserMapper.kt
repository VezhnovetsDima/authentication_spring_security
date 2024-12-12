package com.example.security.mapper

import com.example.security.dto.UserDto
import com.example.security.entity.Role
import com.example.security.entity.UserEntity
import com.example.security.service.JwtService
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.springframework.beans.factory.annotation.Autowired


@Mapper(componentModel = "spring")
abstract class UserMapper {

    @Autowired
    val jwtService: JwtService? = null

    @Mapping(source = "encryptedPassword", target = "password")
    @Mapping(target = "role", source = "role", qualifiedByName = ["mapRoleToString"])
    abstract fun toDto(entity: UserEntity): UserDto

    @Mapping(source = "password", target = "encryptedPassword", qualifiedByName = ["mapPassword"])
    @Mapping(target = "role", source = "role", qualifiedByName = ["mapRole"])
    abstract fun toEntity(dto: UserDto): UserEntity

    @Named("mapRole")
    fun mapRole(role: String): Role {
        return Role.entries.firstOrNull { it.name == role }
            ?: throw IllegalArgumentException("Invalid role name: $role")
    }

    @Named("mapRoleToString")
    fun mapRoleToString(role: Role): String {
        return role.name
    }

    @Named("mapPassword")
    fun mapPassword(password: String): String {
        return jwtService?.encodePassword(password) ?: throw IllegalStateException("Failed to encode password")
    }
}

