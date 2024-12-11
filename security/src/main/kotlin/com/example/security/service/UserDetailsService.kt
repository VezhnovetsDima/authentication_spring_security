package com.example.security.service

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
        return repository.findByEmail(username ?: throw IllegalArgumentException("Empty Username")) ?: throw UsernameNotFoundException("Can't find user with Usename")
    }
}