package com.example.nutritive_app.controller

import com.example.nutritive_app.dto.request.DeviceTokenRequest
import com.example.nutritive_app.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/devices")
class DeviceController(
    private val userService: UserService
) {
    @PostMapping("/register")
    fun registerDevice(@RequestBody request: DeviceTokenRequest, principal: Principal): ResponseEntity<Void> {
        println("📱 Register device called")
        println("🔑 Principal = ${principal.name}")
        println("📨 Incoming token = ${request.token}")

        val user = userService.getUserByEmail(principal.name)
        user.deviceToken = request.token
        userService.save(user)

        println("✅ Device token saved for user ${user.id}")

        return ResponseEntity.ok().build()
    }
}
