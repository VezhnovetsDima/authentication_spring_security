package com.example.security.controller

import com.example.security.dto.AuthenticationRole
import com.example.security.dto.UserRolesDto
import com.example.security.service.AdminService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController(
    private val adminService: AdminService
) {

    @PostMapping("/set_role")
    fun setRole(@RequestBody auth: AuthenticationRole): UserRolesDto {
        return adminService.authenticateAndSetRole(auth)
    }

    @PostMapping("remove_role")
    fun removeRole(@RequestBody auth: AuthenticationRole): UserRolesDto {
        return adminService.removeRole(auth)
    }
}