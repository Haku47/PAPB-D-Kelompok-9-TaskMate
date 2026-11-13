package com.example.taskmate.data.remote.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class QuoteDto(
    @SerializedName("quote") val quote: String = "",
    @SerializedName("author") val author: String = ""
)

/**
 * Interface Retrofit HTTP API Client (Materi PAPB12 - Retrofit API).
 * Bertanggung jawab mengambil data kutipan motivasi harian dari REST API web service.
 */
interface QuoteApiService {

    @GET("quotes/random")
    suspend fun getRandomQuote(): Response<QuoteDto>

    companion object {
        private const val BASE_URL = "https://dummyjson.com/"

        fun create(): QuoteApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuoteApiService::class.java)
        }
    }
}
