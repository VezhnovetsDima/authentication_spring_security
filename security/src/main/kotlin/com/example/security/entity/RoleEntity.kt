package com.example.security.entity

import jakarta.persistence.*
import lombok.AccessLevel
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import lombok.experimental.FieldDefaults
import org.springframework.security.core.GrantedAuthority

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
class RoleEntity(var name: String?) : GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    override fun getAuthority(): String {
        return id.toString()
    }
}