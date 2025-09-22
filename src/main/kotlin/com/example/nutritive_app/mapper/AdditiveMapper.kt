package com.example.nutritive_app.mapper

import com.example.nutritive_app.dto.AdditiveDTO
import com.example.nutritive_app.entity.Additive
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface AdditiveMapper {
    fun toDto(entity: Additive): AdditiveDTO
    fun toEntity(dto: AdditiveDTO): Additive
    fun toDtoList(entities: List<Additive>): List<AdditiveDTO>
}