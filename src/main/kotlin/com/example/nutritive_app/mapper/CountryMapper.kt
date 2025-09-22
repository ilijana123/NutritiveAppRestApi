package com.example.nutritive_app.mapper

import com.example.nutritive_app.dto.CountryDTO
import com.example.nutritive_app.entity.Country
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface CountryMapper {
    fun toDto(entity: Country): CountryDTO
    fun toEntity(dto: CountryDTO): Country
    fun toDtoList(entities: List<Country>): List<CountryDTO>
}