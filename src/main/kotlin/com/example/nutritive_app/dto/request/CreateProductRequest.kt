package com.example.nutritive_app.dto.request

data class CreateProductRequest(
    val barcode: String,
    val name: String,
    val allergenIds: List<Long> = emptyList(),
    val additiveIds: List<Long> = emptyList(),
    val tagIds: List<Long> = emptyList(),
    val categoryIds: List<Long> = emptyList(),
    val countryIds: List<Long> = emptyList(),
    val carbohydrates: Float? = null,
    val carbohydrates_100g: Float? = null,
    val energy: Float? = null,
    val energy_kj_100g: Float? = null,
    val fat: Float? = null,
    val fat_100g: Float? = null,
    val fiber: Float? = null,
    val fiber_100g: Float? = null,
    val proteins: Float? = null,
    val proteins_100g: Float? = null,
    val salt: Float? = null,
    val salt_100g: Float? = null,
    val saturated_fat: Float? = null,
    val saturated_fat_100g: Float? = null,
    val sodium: Float? = null,
    val sodium_100g: Float? = null,
    val sugars: Float? = null,
    val sugars_100g: Float? = null
)