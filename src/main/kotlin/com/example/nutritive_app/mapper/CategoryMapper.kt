package com.example.nutritive_app.mapper

import com.example.nutritive_app.dto.CategoryDTO
import com.example.nutritive_app.entity.Category
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface CategoryMapper {
    fun toDto(entity: Category): CategoryDTO
    fun toEntity(dto: CategoryDTO): Category
    fun toDtoList(entities: List<Category>): List<CategoryDTO>
}

