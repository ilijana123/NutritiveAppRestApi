package com.example.nutritive_app.service

import com.example.nutritive_app.dto.CategoryDTO
import com.example.nutritive_app.entity.Category
import com.example.nutritive_app.exception.CategoryNotFoundException
import com.example.nutritive_app.mapper.CategoryMapper
import com.example.nutritive_app.repository.CategoryRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val mapper: CategoryMapper
) {

    fun getAllCategories(): List<CategoryDTO> = mapper.toDtoList(categoryRepository.findAll())

    fun getCategoriesById(categoryId: Long): Category = categoryRepository.findById(categoryId)
        .orElseThrow { CategoryNotFoundException(HttpStatus.NOT_FOUND, "No matching category was found") }

    fun createCategory(category: Category): Category = categoryRepository.save(category)

    fun updateCategoryById(categoryId: Long, category: Category): Category {
        return if (categoryRepository.existsById(categoryId)) {
            categoryRepository.save(
                Category(
                    id = categoryId,
                    name = category.name,
                    products = category.products,
                )
            )
        } else throw CategoryNotFoundException(HttpStatus.NOT_FOUND, "No matching category was found")
    }

    fun deleteCategoriesById(categoryId: Long) {
        return if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId)
        } else throw CategoryNotFoundException(HttpStatus.NOT_FOUND, "No matching category was found")
    }

    fun findOrCreate(name: String): Category {
        return categoryRepository.findByName(name) ?: categoryRepository.save(Category(name = name))
    }

    fun findOrCreateAll(names: List<String>?): MutableSet<Category> {
        if (names == null) return mutableSetOf()
        return names
            .filter { it.startsWith("en:")}
            .map { findOrCreate(it.removePrefix("en:")
                .trim()
                .split('-')
                .joinToString(" ") { word -> word.replaceFirstChar { it.uppercaseChar() } })}
            .toMutableSet()
    }

    fun findAllByIds(ids: List<Long>): Set<Category> {
        return categoryRepository.findAllById(ids).toSet()
    }
}
