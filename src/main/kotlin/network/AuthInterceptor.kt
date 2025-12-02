package org.example.network

import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = tokenProvider()

        val builder = original.newBuilder()
            .header("Accept", "application/json")

        if (!token.isNullOrBlank()) {
            builder.header("Authorization", "Bearer $token")
        }

        val request = builder.build()

        println("\n========== [REQUEST DEBUG] ==========")
        println("URL: ${request.url}")
        println("METHOD: ${request.method}")
        println("HEADERS:\n${request.headers}")

        // Log do corpo da requisição
        if (request.body != null) {
            val buffer = Buffer()
            request.body!!.writeTo(buffer)
            println("BODY: ${buffer.readUtf8()}")
        } else {
            println("BODY: <empty>")
        }
        println("======================================")

        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            println("\n❌ ERRO NA REQUISIÇÃO: ${e.message}")
            throw e
        }

        println("\n========== [RESPONSE DEBUG] ==========")
        println("URL: ${response.request.url}")
        println("STATUS CODE: ${response.code}")
        println("MESSAGE: ${response.message}")
        println("HEADERS:\n${response.headers}")
        println("======================================\n")

        return response
    }
}
