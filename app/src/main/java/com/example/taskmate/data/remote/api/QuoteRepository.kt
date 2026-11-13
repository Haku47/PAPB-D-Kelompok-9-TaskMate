package com.example.taskmate.data.remote.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositori untuk mengambil data quote motivasi dari Retrofit REST API.
 */
class QuoteRepository(
    private val apiService: QuoteApiService = QuoteApiService.create()
) {
    suspend fun fetchDailyMotivationQuote(): Pair<String, String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getRandomQuote()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Pair(body.quote, body.author)
            } else {
                Pair("Continuous learning is the key to unlocking potential.", "Winston Churchill")
            }
        } catch (_: Exception) {
            Pair("Success is not final, failure is not fatal: it is the courage to continue that counts.", "Winston Churchill")
        }
    }
}
