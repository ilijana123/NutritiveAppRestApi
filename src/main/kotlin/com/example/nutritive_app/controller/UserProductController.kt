package com.example.nutritive_app.controller

import com.example.nutritive_app.service.UserProductService
import com.example.nutritive_app.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class UserProductController(
    private val userProductService: UserProductService,
    private val userService: UserService
) {
    @PostMapping("/{barcode}/check-allergens")
    fun checkAllergens(
        @PathVariable barcode: String,
        authentication: Authentication
    ): ResponseEntity<Void> {
        println("🔎 checkAllergens called with barcode=$barcode")

        val username = authentication.name
        println("👤 Principal name = $username")

        val user = try {
            userService.getUserByEmail(authentication.name)
        } catch (e: Exception) {
            println("❌ Failed to load user: ${e.message}")
            return ResponseEntity.status(404).build()
        }

        println("✅ User resolved: id=${user.id}, username=${user.username}")

        try {
            userProductService.checkAndNotify(user.id!!, barcode)
        } catch (e: Exception) {
            println("💥 Error in checkAndNotify: ${e.message}")
            e.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }

        return ResponseEntity.ok().build()
    }

    @PostMapping("/{barcode}/save")
    fun saveProduct(
        @PathVariable barcode: String,
        authentication: Authentication
    ): ResponseEntity<Void> {
        val user = userService.getUserByEmail(authentication.name)

        try {
            userProductService.saveProductForUser(user.id!!, barcode)
        } catch (e: Exception) {
            println("❌ Failed to save product: ${e.message}")
            return ResponseEntity.internalServerError().build()
        }

        return ResponseEntity.ok().build()
    }

    @GetMapping("/saved")
    fun getSavedProducts(authentication: Authentication): ResponseEntity<Any> {
        val user = userService.getUserByEmail(authentication.name)

        return try {
            val products = userProductService.getSavedProducts(user.id!!)
            ResponseEntity.ok(products)
        } catch (e: Exception) {
            println("❌ Failed to fetch saved products: ${e.message}")
            ResponseEntity.internalServerError().body("Failed to load saved products")
        }
    }
}
