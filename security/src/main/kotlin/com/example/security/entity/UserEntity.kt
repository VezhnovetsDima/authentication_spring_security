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

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinTable(name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")])
    var roles: MutableSet<RoleEntity> = mutableSetOf()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.map{ SimpleGrantedAuthority(it.name) }.toMutableList()
    }

    override fun getPassword(): String {
        return encryptedPassword
    }

    override fun getUsername(): String {
        return email ?: "undefined";
    }
}