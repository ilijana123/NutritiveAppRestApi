package com.example.nutritive_app.controller

import com.example.nutritive_app.dto.request.AssignRoleRequest
import com.example.nutritive_app.dto.request.LoginRequest
import com.example.nutritive_app.dto.request.SignUpRequest
import com.example.nutritive_app.dto.request.UpdateUserRequest
import com.example.nutritive_app.dto.response.AuthResponse
import com.example.nutritive_app.jwt.JwtUtil
import com.example.nutritive_app.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val jwtUtil: JwtUtil,
    private val authenticationManager: AuthenticationManager
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/create")
    fun createUser(@RequestBody request: SignUpRequest): ResponseEntity<AuthResponse> {
        val user = userService.createUser(request)
        val token = jwtUtil.generateToken(user.email)

        val response = AuthResponse(
            id = user.id!!,
            email = user.email,
            username = user.username,
            firstName = user.firstName,
            lastName = user.lastName,
            token = token
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping("/{userId}/roles")
    fun assignRoleToUser(@PathVariable userId: Long, @RequestParam role: AssignRoleRequest): ResponseEntity<String> {
        userService.assignRoleToUser(userId, role.roleId.toString())
        return ResponseEntity.ok("User with id $userId has been assigned the role $role")
    }

    @GetMapping("/{userId}/roles")
    fun getUserRoles(@PathVariable userId: Long): ResponseEntity<Set<String>> {
        val roles = userService.getUserRoles(userId)
        return ResponseEntity.ok(roles)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<*> {
        return try {
            val auth: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )
            val userDetails = auth.principal as UserDetails
            val user = userService.getUserByEmail(userDetails.username) // fetch actual user
            val token = jwtUtil.generateToken(userDetails.username)
            ResponseEntity.ok(AuthResponse(
                user.id!!,
                user.email,
                user.username,
                firstName = user.firstName,
                lastName = user.lastName,
                token = token
            ))
        } catch (_: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password")
        }
    }
    @PutMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: Long,
        @RequestBody request: UpdateUserRequest
    ): ResponseEntity<*> {
        return try {
            val updatedUser = userService.updateUser(userId, request)
            val newToken = jwtUtil.generateToken(updatedUser.email)
            val response = AuthResponse(
                id = updatedUser.id!!,
                email = updatedUser.email,
                username = updatedUser.username,
                firstName = updatedUser.firstName,
                lastName = updatedUser.lastName,
                token = newToken
            )
            ResponseEntity.ok(response)
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        } catch (e: Exception) {
            logger.error("Error updating user with id $userId", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while updating user")
        }
    }
}
