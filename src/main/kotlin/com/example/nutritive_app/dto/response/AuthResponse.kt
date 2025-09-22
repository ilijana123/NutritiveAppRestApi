package com.example.nutritive_app.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthResponse(
    @JsonProperty("id") val id: Long,
    @JsonProperty("email") val email: String,
    @JsonProperty("username") val username: String,
    @JsonProperty("firstName") var firstName: String,
    @JsonProperty("lastName") var lastName: String,
    @JsonProperty("token")val token: String)