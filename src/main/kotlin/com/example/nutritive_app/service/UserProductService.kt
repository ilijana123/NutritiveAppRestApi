package com.example.nutritive_app.service

import com.example.nutritive_app.dto.ProductDetailsDTO
import com.example.nutritive_app.mapper.toDetailsDTO
import com.example.nutritive_app.repository.ProductRepository
import com.example.nutritive_app.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserProductService(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val fcmService: FcmService,
    private val userService: UserService
) {
    fun checkAndNotify(userId: Long, barcode: String) {
        val user = userRepository.findById(userId).orElseThrow()
        val product = productRepository.findByBarcode(barcode)

        println("📦 Found product: ${product?.barcode} -> ${product?.name}")
        println("🧑 User: ${user.username}, deviceToken=${user.deviceToken}")

        val userAllergens = user.allergens.map { it.name.lowercase() }
        val productAllergens = product?.allergens?.map { it.name.lowercase() }.orEmpty()

        println("🔎 User allergens = $userAllergens")
        println("🔎 Product allergens = $productAllergens")

        val matches = userAllergens.intersect(productAllergens.toSet())
        println("⚠️ Matches = $matches")

        if (matches.isNotEmpty()) {
            if (user.deviceToken != null) {
                try {
                    fcmService.sendAllergenAlert(user.deviceToken!!, product!!.name, matches.toList())
                    println("📤 Sent allergen alert")
                } catch (e: Exception) {
                    println("💥 FCM send failed: ${e.message}")
                    e.printStackTrace()
                }
            } else {
                println("ℹ️ User has no device token set!")
            }
        } else {
            println("ℹ️ No allergen match, no alert")
        }
    }
    @Transactional
    fun saveProductForUser(userId: Long, barcode: String) {
        val user = userRepository.findById(userId).orElseThrow()
        val product = productRepository.findById(barcode).orElse(null)

        if (!user.products.contains(product)) {
            user.products.add(product)
            userRepository.save(user)
        }
    }

    fun getSavedProducts(userId: Long): List<ProductDetailsDTO> {
        val user = userService.getUserById(userId)
        return user.products.map { it.toDetailsDTO() }
    }
}
