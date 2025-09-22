package com.example.nutritive_app.service

import com.example.nutritive_app.dto.request.SignUpRequest
import com.example.nutritive_app.dto.request.UpdateUserRequest
import com.example.nutritive_app.entity.User
import com.example.nutritive_app.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun createUser(request: SignUpRequest): User {
        if (userRepository.existsByEmail(request.email)) {
            throw RuntimeException("Email already exists")
        }

        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            username = request.username,
            firstName = request.firstName,
            lastName = request.lastName,
            roles = setOf("USER")
        )
        return userRepository.save(user)
    }

    fun assignRoleToUser(userId: Long, role: String) {
        val user = userRepository.findById(userId).orElseThrow {
            NoSuchElementException("User not found with ID $userId")
        }

        val updatedRoles = user.roles.toMutableSet()
        updatedRoles.add(role)

        val updatedUser = user.copy(roles = updatedRoles)
        userRepository.save(updatedUser)
    }

    fun getUserRoles(userId: Long): Set<String> {
        val user = userRepository.findById(userId).orElseThrow {
            NoSuchElementException("User not found with ID $userId")
        }
        return user.roles
    }

    fun getUserByEmail(email: String): User =
        userRepository.findByEmail(email)
            ?: throw NoSuchElementException("User not found with email $email")

    fun getUserById(id: Long): User =
        userRepository.findById(id).orElseThrow {
            NoSuchElementException("User not found with id $id")
        }

    fun save(user: User) = userRepository.save(user)

    fun updateUser(userId: Long, request: UpdateUserRequest): User {
        val user = userRepository.findById(userId).orElseThrow {
            NoSuchElementException("User not found with ID $userId")
        }

        if (request.email != null && request.email != user.email) {
            if (userRepository.existsByEmail(request.email)) {
                throw IllegalArgumentException("Email already exists")
            }
        }

        if (request.username != null && request.username != user.username) {
            if (userRepository.existsByUsername(request.username)) {
                throw IllegalArgumentException("Username already exists")
            }
        }

        val updatedUser = user.copy(
            email = request.email ?: user.email,
            firstName = request.firstName ?: user.firstName,
            lastName = request.lastName ?: user.lastName,
            username = request.username ?: user.username
        )

        return userRepository.save(updatedUser)
    }
}
