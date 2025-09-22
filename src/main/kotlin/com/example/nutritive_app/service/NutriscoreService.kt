package com.example.nutritive_app.service

import com.example.nutritive_app.dto.NutriscoreDTO
import com.example.nutritive_app.entity.Nutriscore
import com.example.nutritive_app.repository.NutriscoreRepository
import org.json.JSONObject
import org.springframework.stereotype.Service
import org.springframework.http.HttpStatus
import com.example.nutritive_app.exception.NutriscoreNotFoundException

@Service
class NutriscoreService(
    private val nutriscoreRepository: NutriscoreRepository
) {

    fun getAllNutriscores(): List<Nutriscore> = nutriscoreRepository.findAll()

    fun getNutriscoresById(id: Long): Nutriscore =
        nutriscoreRepository.findById(id).orElseThrow {
            NoSuchElementException("Nutriscore with id $id not found")
        }

    fun createNutriscore(nutriscore: Nutriscore): Nutriscore = nutriscoreRepository.save(nutriscore)

    fun updateNutriscoreById(id: Long, updated: Nutriscore): Nutriscore {
        val existing = getNutriscoresById(id)
        val toSave = existing.copy(
            grade = updated.grade,
            score = updated.score,
            positivePoints = updated.positivePoints,
            negativePoints = updated.negativePoints
        )
        return nutriscoreRepository.save(toSave)
    }

    fun deleteNutriscoresById(nutriscoreId: Long) {
        return if(nutriscoreRepository.existsById(nutriscoreId)) {
            nutriscoreRepository.deleteById(nutriscoreId)
        } else throw NutriscoreNotFoundException(HttpStatus.NOT_FOUND, "No matching nutriscore was found")
    }

    fun findOrCreate(dto: NutriscoreDTO?): Nutriscore {
        val nutriscore = Nutriscore(
            grade = dto?.grade,
            score = dto?.score,
            positivePoints = dto?.positivePoints,
            negativePoints = dto?.negativePoints
        )
        return nutriscoreRepository.save(nutriscore)
    }

    fun extractAndSaveFromJson(json: JSONObject?): Nutriscore? {
        if (json == null) return null

        // --- 1. Try year-based nutriscore (2023, 2021, …) ---
        val years = json.keys().asSequence()
            .mapNotNull { it.toString().toIntOrNull() }
            .toList()

        if (years.isNotEmpty()) {
            val latestYear = years.maxOrNull()!!
            val yearData = json.optJSONObject(latestYear.toString())
            if (yearData != null) {
                val grade = yearData.optString("grade", null)
                val score = if (yearData.has("score")) yearData.optInt("score") else null
                val data = yearData.optJSONObject("data")
                val positivePoints = data?.optInt("positive_points")
                val negativePoints = data?.optInt("negative_points")

                return nutriscoreRepository.save(
                    Nutriscore(
                        grade = grade,
                        score = score,
                        positivePoints = positivePoints,
                        negativePoints = negativePoints
                    )
                )
            }
        }

        // --- 2. Fallback: use flat nutriscore fields ---
        val grade = if (json.has("nutriscore_grade")) json.optString("nutriscore_grade") else null
        val score = if (json.has("nutriscore_score")) json.optInt("nutriscore_score") else null
        val data = json.optJSONObject("nutriscore_data")

        val positivePoints = data?.optInt("positive_points")
        val negativePoints = data?.optInt("negative_points")

        if (grade != null || score != null || data != null) {
            return nutriscoreRepository.save(
                Nutriscore(
                    grade = grade,
                    score = score,
                    positivePoints = positivePoints,
                    negativePoints = negativePoints
                )
            )
        }

        return null
    }
}
