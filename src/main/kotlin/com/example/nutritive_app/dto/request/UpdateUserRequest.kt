package com.example.nutritive_app.dto.request

data class UpdateUserRequest(
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val username: String?
)