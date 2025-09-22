package com.example.nutritive_app.dto.request

data class ProductFilterRequest(
    val allergenIds: List<Long> = emptyList(),
    val additiveIds: List<Long> = emptyList(),
    val countryIds: List<Long> = emptyList(),
    val categoryIds: List<Long> = emptyList(),
    val tagIds: List<Long> = emptyList(),
    val searchTerm: String? = null,
    val page: Int = 0,
    val size: Int = 20
)