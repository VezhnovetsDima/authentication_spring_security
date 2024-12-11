package com.example.security.entity

import jakarta.persistence.*
import lombok.*
import lombok.experimental.FieldDefaults
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserEntity : UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    var firstName: String? = null

    var lastName: String? = null

    var email: String? = null

    var encryptedPassword : String = ""

    @Enumerated(EnumType.STRING)
    var role: Role? = null

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role?.name))
    }

    override fun getPassword(): String {
        return encryptedPassword
    }

    override fun getUsername(): String {
        return email ?: "undefined";
    }
}