package com.example.nutritive_app.service

import com.google.auth.oauth2.GoogleCredentials
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.stereotype.Service

@Service
class FcmService {

    private val httpClient = OkHttpClient()
    private val fcmUrl = "https://fcm.googleapis.com/v1/projects/nutritiveapp/messages:send"

    private val credentials: GoogleCredentials = GoogleCredentials
        .getApplicationDefault()
        .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))

    fun sendNotification(targetToken: String, title: String, body: String) {
        credentials.refreshIfExpired()
        val accessToken = credentials.accessToken.tokenValue

        val message = JsonObject().apply {
            add("message", JsonObject().apply {
                addProperty("token", targetToken)
                add("notification", JsonObject().apply {
                    addProperty("title", title)
                    addProperty("body", body)
                })
            })
        }

        val requestBody = message.toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(fcmUrl)
            .addHeader("Authorization", "Bearer $accessToken")
            .post(requestBody)
            .build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw RuntimeException("FCM send failed: ${response.code} - ${response.message}")
            }
        }
    }

    fun sendAllergenAlert(deviceToken: String, productName: String?, allergens: List<String>) {
        sendNotification(
            targetToken = deviceToken,
            title = "⚠️ Allergen Alert",
            body = "Product $productName contains: ${allergens.joinToString()}"
        )
    }
}

