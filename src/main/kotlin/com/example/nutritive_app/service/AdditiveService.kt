package com.example.nutritive_app.service

import com.example.nutritive_app.dto.AdditiveDTO
import com.example.nutritive_app.entity.Additive
import com.example.nutritive_app.exception.AdditiveNotFoundException
import com.example.nutritive_app.mapper.AdditiveMapper
import com.example.nutritive_app.repository.AdditiveRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
@Service
class AdditiveService(
    private val additiveRepository: AdditiveRepository,
    private val mapper: AdditiveMapper,
) {

    fun getAllAdditives(): List<AdditiveDTO> = mapper.toDtoList(additiveRepository.findAll())

    fun getAdditivesById(additiveId: Long): Additive = additiveRepository.findById(additiveId)
        .orElseThrow { AdditiveNotFoundException(HttpStatus.NOT_FOUND, "No matching additive was found") }

    fun createAdditive(additive: Additive): Additive = additiveRepository.save(additive)

    fun updateAdditiveById(additiveId: Long, additive: Additive): Additive {
        return if (additiveRepository.existsById(additiveId)) {
            additiveRepository.save(
                Additive(
                    id = additiveId,
                    name = additive.name,
                    products = additive.products,
                )
            )
        } else throw AdditiveNotFoundException(HttpStatus.NOT_FOUND, "No matching additive was found")
    }

    fun deleteAdditivesById(additiveId: Long) {
        return if (additiveRepository.existsById(additiveId)) {
            additiveRepository.deleteById(additiveId)
        } else throw AdditiveNotFoundException(HttpStatus.NOT_FOUND, "No matching additive was found")
    }

    fun findOrCreate(name: String): Additive {
        return additiveRepository.findByName(name) ?: additiveRepository.save(Additive(name = name))
    }

    fun findOrCreateAll(names: List<String>?): MutableSet<Additive> {
        if (names == null) return mutableSetOf()
        return names
            .filter { it.startsWith("en:")}
            .map { findOrCreate(it.removePrefix("en:").trim())}
            .toMutableSet()
    }

    fun findAllByIds(ids: List<Long>): Set<Additive> {
        return additiveRepository.findAllById(ids).toSet()
    }
}
