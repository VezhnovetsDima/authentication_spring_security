package com.example.security.mapper

import com.example.security.dto.UserDto
import com.example.security.entity.Role
import com.example.security.entity.UserEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(componentModel = "spring")
interface UserMapper {

    @Mapping(source = "encryptedPassword", target = "password")
    @Mapping(target = "role", source = "role", qualifiedByName = ["mapRoleToString"])
    fun toDto(entity: UserEntity): UserDto

    @Mapping(source = "password", target = "encryptedPassword")
    @Mapping(target = "role", source = "role", qualifiedByName = ["mapRole"])
    fun toEntity(dto: UserDto): UserEntity

    @Named("mapRole")
    fun mapRole(role: String): Role {
        return Role.entries.firstOrNull { it.name == role }
            ?: throw IllegalArgumentException("Invalid role name: $role")
    }

    @Named("mapRoleToString")
    fun mapRoleToString(role: Role): String {
        return role.name
    }
}

