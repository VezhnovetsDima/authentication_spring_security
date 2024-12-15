package com.example.security.mapper

import com.example.security.dto.UserDto
import com.example.security.entity.UserEntity
import com.example.security.service.AuthService
import com.example.security.service.JwtService
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.springframework.beans.factory.annotation.Autowired


@Mapper(componentModel = "spring")
abstract class UserMapper {

    @Mapping(source = "encryptedPassword", target = "password", qualifiedByName = ["passwordMapping"])
    abstract fun toDto(entity: UserEntity): UserDto

    @Mapping(source = "password", target = "encryptedPassword")
    abstract fun toEntity(dto: UserDto): UserEntity


    @Named("passwordMapping")
    fun passwordMapping(encryptedPassword: String): String {
        return ""
    }
}

