package org.example.api

import org.example.model.*
import retrofit2.Response
import retrofit2.http.*

interface ZenUpApi {
    @POST("api/login")
    suspend fun login(@Body req: LoginRequest): Response<LoginResponse>

    @POST("api/chat")
    suspend fun chat(@Body req: ChatRequest): Response<ChatResponse>

    @GET("api/resumo/{id_usuario}")
    suspend fun resumo(@Path("id_usuario") id: Long): Response<ResumoResponse>
}
