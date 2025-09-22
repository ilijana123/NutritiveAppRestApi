package com.example.nutritive_app.repository

import com.example.nutritive_app.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    override fun findAll(): List<User>
    fun findByUsername(username: String): User
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User?
    fun existsByUsername(username: String): Boolean
}