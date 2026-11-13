package com.example.taskmate.data.remote.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class RemoteTaskDto(
    @SerializedName("id") val id: Int,
    @SerializedName("todo") val todo: String,
    @SerializedName("completed") val completed: Boolean
)

data class RemoteTaskResponse(
    @SerializedName("todos") val todos: List<RemoteTaskDto> = emptyList()
)

// Interface Retrofit API untuk sync tugas dari server
interface TaskApiService {

    @GET("todos?limit=5")
    suspend fun fetchRemoteTasks(): Response<RemoteTaskResponse>

    companion object {
        private const val BASE_URL = "https://dummyjson.com/"

        fun create(): TaskApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TaskApiService::class.java)
        }
    }
}
