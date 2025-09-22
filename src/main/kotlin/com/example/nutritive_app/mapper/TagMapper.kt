package com.example.nutritive_app.mapper

import com.example.nutritive_app.dto.TagDTO
import com.example.nutritive_app.entity.Tag
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface TagMapper {
    fun toDto(entity: Tag): TagDTO
    fun toEntity(dto: TagDTO): Tag
    fun toDtoList(entities: List<Tag>): List<TagDTO>
}