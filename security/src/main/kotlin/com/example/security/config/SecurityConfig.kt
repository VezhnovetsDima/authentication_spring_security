package com.example.security.config

import com.example.security.entity.RoleEntity
import com.example.security.entity.UserEntity
import com.example.security.repository.RoleRepository
import com.example.security.repository.UserRepository
import com.example.security.service.AuthService
import com.example.security.service.JwtService
import com.example.security.service.UserDetailsService
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/*
{
    "username": "john@example.com",
    "setRole": "ADMIN"
}

{
    "username": "admin@email.ru",
    "password": "123"
}

 */

@Configuration
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthenticationConfig,
    private val userDetailsService: UserDetailsService,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val authService: AuthService,
) {

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val daoAuthProvider = DaoAuthenticationProvider()
        daoAuthProvider.setUserDetailsService(userDetailsService)
        daoAuthProvider.setPasswordEncoder(BCryptPasswordEncoder())
        return daoAuthProvider
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    @Bean
    @Transactional
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val roles = mutableListOf(
            RoleEntity(name = "ROLE_ADMIN"),
            RoleEntity(name = "ROLE_USER"),
            RoleEntity(name = "ROLE_SUPPORT")
        )
        roleRepository.saveAllAndFlush(roles)

        val userEntity = UserEntity()
        userEntity.firstName = "admin"
        userEntity.lastName = "admin"
        userEntity.email = "admin@email.ru"
        userEntity.encryptedPassword = authService.encrypt("123")
        userEntity.roles = mutableSetOf(roleRepository.findByName("ROLE_ADMIN")!!)
        userRepository.saveAndFlush(userEntity)

        http
            .csrf{ it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
                    .requestMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                    .anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .logout {
                it.logoutUrl("/auth/logout")
                it.invalidateHttpSession(true)
                it.deleteCookies("JSESSIONID")
            }
        return http.build()
    }
}
