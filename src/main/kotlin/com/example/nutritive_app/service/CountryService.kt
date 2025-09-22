package com.example.nutritive_app.service

import com.example.nutritive_app.dto.CountryDTO
import com.example.nutritive_app.entity.Country
import com.example.nutritive_app.exception.CountryNotFoundException
import com.example.nutritive_app.mapper.CategoryMapper
import com.example.nutritive_app.mapper.CountryMapper
import com.example.nutritive_app.repository.CountryRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class CountryService(
    private val countryRepository: CountryRepository,
    private val mapper: CountryMapper
) {

    fun getAllCountries(): List<CountryDTO> = mapper.toDtoList(countryRepository.findAll())

    fun getCountriesById(countryId: Long): Country = countryRepository.findById(countryId)
        .orElseThrow { CountryNotFoundException(HttpStatus.NOT_FOUND, "No matching country was found") }

    fun createCountry(country: Country): Country = countryRepository.save(country)

    fun updateCountryById(countryId: Long, country: Country): Country {
        return if (countryRepository.existsById(countryId)) {
            countryRepository.save(
                Country(
                    id = countryId,
                    name = country.name,
                    products = country.products,
                )
            )
        } else throw CountryNotFoundException(HttpStatus.NOT_FOUND, "No matching country was found")
    }

    fun deleteCountriesById(countryId: Long) {
        return if (countryRepository.existsById(countryId)) {
            countryRepository.deleteById(countryId)
        } else throw CountryNotFoundException(HttpStatus.NOT_FOUND, "No matching country was found")
    }

    fun findOrCreate(name: String): Country {
        return countryRepository.findByName(name) ?: countryRepository.save(Country(name = name))
    }

    fun findOrCreateAll(names: List<String>?): MutableSet<Country> {
        if (names == null) return mutableSetOf()
        return names
            .filter { it.startsWith("en:")}
            .map { findOrCreate(it.removePrefix("en:")
                .trim()
                .split('-')
                .joinToString(" ") { word -> word.replaceFirstChar { it.uppercaseChar() } })}
            .toMutableSet()
    }

    fun findAllByIds(ids: List<Long>): Set<Country> {
        return countryRepository.findAllById(ids).toSet()
    }
}
