package com.example.security.repository

import com.example.security.entity.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<RoleEntity, Long> {

    fun findByName(name: String): RoleEntity?
}