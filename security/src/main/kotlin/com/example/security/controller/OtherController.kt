package com.example.security.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/books")
class OtherController {

    @GetMapping("/get")
    fun getUserBookByUserEmail(@RequestBody email: String): String {
        return email;//todo
    }
}