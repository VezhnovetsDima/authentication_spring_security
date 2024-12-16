package com.example.security.service

import com.example.security.exception.EmptyFieldException
import com.example.security.exception.UserNotExistsException
import com.example.security.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsService(
    private val repository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        return repository.findByEmail(username ?: throw EmptyFieldException()) ?: throw UserNotExistsException("Can't find user with username UserDetailsService.loadUserByUsername()")
    }
}